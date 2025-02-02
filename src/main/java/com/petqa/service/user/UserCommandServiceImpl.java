package com.petqa.service.user;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petqa.apiPayload.apiPayload.code.status.ErrorStatus;
import com.petqa.apiPayload.apiPayload.exception.handler.FileUploadHandler;
import com.petqa.apiPayload.apiPayload.exception.handler.TokenHandler;
import com.petqa.apiPayload.apiPayload.exception.handler.UserHandler;
import com.petqa.domain.Pet;
import com.petqa.domain.User;
import com.petqa.domain.enums.Gender;
import com.petqa.domain.enums.PetType;
import com.petqa.dto.auth.AuthRequestDTO;
import com.petqa.dto.auth.AuthResponseDTO;
import com.petqa.dto.user.UserRequestDTO;
import com.petqa.repository.PetRepository;
import com.petqa.repository.RefreshRepository;
import com.petqa.repository.UserRepository;
import com.petqa.security.jwt.JwtUtil;
import com.petqa.service.refresh.RefreshCommandService;
import com.petqa.service.s3Bucket.S3Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

	private final UserRepository userRepository;
	private final PetRepository petRepository;

	private final S3Service s3Service;

	private final JwtUtil jwtUtil;

	private final RefreshCommandService refreshCommandService;
	private final RefreshRepository refreshRepository;

	@Value("${spring.jwt.accessTokenExpiration}")
	private long accessTokenExpiration;

	@Value("${spring.jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;

	@Override
	public AuthResponseDTO.LoginResponseDTO login(AuthRequestDTO.SocialLoginDTO socialLoginDTO) {
		String socialId = socialLoginDTO.getSocialId();
		String username = socialLoginDTO.getUsername();

		User user;
		/*
		 * 유저가 존재하지 않는다면
		 * isSuccess = false가 response로 넘어가고
		 * 회원가입으로 넘어가야함
		 * */
		user = userRepository.findUserAndPetBySocialIdAndUsername(socialId, username)
			.orElseThrow(() ->
				new UserHandler(ErrorStatus.USER_NOT_FOUND)
			);

		String access = jwtUtil.createJwt("access", user.getSocialId(), user.getUsername(), accessTokenExpiration);
		String refresh = jwtUtil.createJwt("refresh", user.getSocialId(), user.getUsername(), refreshTokenExpiration);

		refreshCommandService.addRefresh(username, socialId, refresh, refreshTokenExpiration);

		AuthResponseDTO.PetInfoDTO petInfoDTO = AuthResponseDTO.PetInfoDTO.builder()
			.id(user.getPet().getId())
			.petName(user.getPet().getName())
			.petType(user.getPet().getPetType())
			.build();

		AuthResponseDTO.UserInfoDTO userInfoDTO = AuthResponseDTO.UserInfoDTO.builder()
			.id(user.getId())
			.points(user.getPoints())
			.petInfo(petInfoDTO)
			.build();

		return AuthResponseDTO.LoginResponseDTO.builder()
			.userInfo(userInfoDTO)
			.accessToken(access)
			.refreshToken(refresh)
			.build();
	}

	@Override
	public AuthResponseDTO.LoginResponseDTO join(UserRequestDTO.CreateUserDTO joinRequest) {
		PetType petType = joinRequest.getPetType();
		String socialId = joinRequest.getSocialId();
		String username = joinRequest.getUsername();
		String petName = joinRequest.getPetName();
		LocalDate petBirthday = joinRequest.getPetBirthday();
		Gender petGender = joinRequest.getPetGender();
		Double petWeight = joinRequest.getPetWeight();

		userRepository.findUserBySocialIdAndUsername(socialId, username)
			.ifPresent(u -> {
				throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);
			});

		String uploadedPetProfileImageUrl = null;
		if (joinRequest.getPetProfileImage() != null && !joinRequest.getPetProfileImage().isEmpty()) {
			try {
				uploadedPetProfileImageUrl = s3Service.uploadFile(joinRequest.getPetProfileImage());
			} catch (IOException e) {
				throw new FileUploadHandler(ErrorStatus.FILE_UPLOAD_FAILED);
			}
		}
		String access = jwtUtil.createJwt("access", socialId, username, accessTokenExpiration);
		String refresh = jwtUtil.createJwt("refresh", socialId, username, refreshTokenExpiration);

		refreshCommandService.addRefresh(username, socialId, refresh, refreshTokenExpiration);

		User newUser = User.builder()
			.socialId(socialId)
			.username(username)
			.points(0)
			.questionCount(0)
			.build();

		Pet newPet = Pet.builder()
			.name(petName)
			.petType(petType)
			.birth(petBirthday)
			.profileImage(uploadedPetProfileImageUrl)
			.weight(petWeight)
			.gender(petGender)
			.user(newUser)
			.build();

		// 유저와 펫을 저장
		petRepository.save(newPet);

		AuthResponseDTO.PetInfoDTO petInfoDTO = AuthResponseDTO.PetInfoDTO.builder()
			.id(newPet.getId())
			.petType(petType)
			.petName(petName)
			.build();

		AuthResponseDTO.UserInfoDTO userInfoDTO = AuthResponseDTO.UserInfoDTO.builder()
			.id(newUser.getId())
			.points(newUser.getPoints())
			.petInfo(petInfoDTO)
			.build();

		return AuthResponseDTO.LoginResponseDTO.builder()
			.accessToken(access)
			.refreshToken(refresh)
			.userInfo(userInfoDTO)
			.build();

	}

	@Override
	public AuthResponseDTO.ReissueResponseDTO reissue(String refreshToken) {
		if (refreshToken == null) {
			throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
		}
		try {
			// Check if the token is expired
			if (jwtUtil.isExpired(refreshToken)) {
				throw new TokenHandler(ErrorStatus.REFRESH_TOKEN_EXPIRED);
			}
		} catch (ExpiredJwtException e) {
			// Token has expired
			throw new TokenHandler(ErrorStatus.REFRESH_TOKEN_EXPIRED);
		} catch (JwtException e) {
			// Token is invalid for other reasons
			throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
		} catch (Exception e) {
			// Handle any other exceptions
			throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
		}

		// Extract claims safely
		String category = jwtUtil.getCategory(refreshToken);
		if (!"refresh".equals(category)) {
			throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
		}

		String socialId = jwtUtil.getSocialId(refreshToken);
		String username = jwtUtil.getUsername(refreshToken);
		// 기존 리프레시 토큰 삭제
		refreshRepository.deleteByRefresh(refreshToken);
		// 새로운 리프레시 토큰 생성
		String newRefreshToken = jwtUtil.createJwt("refresh", socialId, username, refreshTokenExpiration);
		// 새로운 리프레시 토큰 저장
		refreshCommandService.addRefresh(username, socialId, newRefreshToken, refreshTokenExpiration);
		// 새로운 액세스 토큰 생성
		String newAccessToken = jwtUtil.createJwt("access", socialId, username, accessTokenExpiration);
		return AuthResponseDTO.ReissueResponseDTO.builder()
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken)
			.build();
	}

	@Override
	public void logout(String refreshToken) {
		if (refreshToken == null) {
			throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
		}

		try {
			jwtUtil.isExpired(refreshToken);
		} catch (Exception e) {
			throw new TokenHandler(ErrorStatus.REFRESH_TOKEN_EXPIRED);
		}

		String category = jwtUtil.getCategory(refreshToken);

		if (!category.equals("refresh")) {
			throw new TokenHandler(ErrorStatus.INVALID_TOKEN);
		}

		String socialId = jwtUtil.getSocialId(refreshToken);
		String username = jwtUtil.getUsername(refreshToken);

		refreshRepository.deleteByRefresh(refreshToken);
	}

	@Override
	public String duplicateCheck(String username) {
		if (username == null || username.isEmpty()) {
			throw new UserHandler(ErrorStatus.USERNAME_IS_NULL);
		}

		userRepository.findUserByUsername(username)
			.ifPresent(u -> {
				throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);
			});
		return "사용 가능한 닉네임입니다.";
	}

}
