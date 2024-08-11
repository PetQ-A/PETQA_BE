package com.petqa.service.mypage;

import com.petqa.converter.DiaryConverter;
import com.petqa.domain.Diary;
import com.petqa.domain.Pet;
import com.petqa.domain.User;
import com.petqa.domain.enums.PetType;
import com.petqa.dto.diary.DiaryResponseDTO;
import com.petqa.dto.mypage.MypageRequestDTO;
import com.petqa.dto.user.UserResponseDTO;
import com.petqa.repository.MypageRepository;
import com.petqa.repository.PetRepository;
import com.petqa.repository.UserRepository;
import com.petqa.security.jwt.JwtUtil;
import com.petqa.service.s3Bucket.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import static com.petqa.converter.MypageConverter.convertToPetDetail;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final MypageRepository mypageRepository;
    private final S3Service s3Service;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDTO.UserDetail showMyInfo(User currentUser){
        Long userId = currentUser.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Pet pet = petRepository.findByUserId(user.getId());

        // UserResponseDTO.UserDetail 객체 생성 및 반환
        return UserResponseDTO.UserDetail.builder()
                .username(user.getUsername())
                .points(user.getPoints()) // points 필드는 User 엔티티에서 정의해야 합니다
                .questionCount(user.getQuestionCount()) // questionCount 필드는 User 엔티티에서 정의해야 합니다
                .petDetail(convertToPetDetail(pet))
                .build();
    }

    @Transactional
    public UserResponseDTO.MypageDetail modifyMyInfo(User currentUser, MypageRequestDTO request, MultipartFile profileImage) {
        // 유저 조회
        Long userId = currentUser.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 반려동물 정보 조회 또는 새로 생성
        Pet pet = petRepository.findByUserId(user.getId());

        String currentName = currentUser.getUsername();
        String newUsername = request.getUsername();
        if (!newUsername.equals(currentName) && mypageRepository.existsByUsername(newUsername)) {
            throw new RuntimeException("이미 존재하는 사용자 이름입니다.");
        }

        // 반려동물 정보 업데이트
        user.setUsername(request.getUsername());
        pet.setName(request.getPetName());
        pet.setPetType(PetType.valueOf(request.getPetType()));
        pet.setBirth(request.getPetBirth());

        // 프로필 이미지 처리
        String imgUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                imgUrl = s3Service.uploadFile(profileImage);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }

        pet.setProfileImage(imgUrl);

        // 반려동물 정보 저장
        userRepository.save(user);
        petRepository.save(pet);

        String newJwtToken = jwtUtil.createJwt(
                "access",
                user.getSocialId(),  // 가정: User 엔티티에 socialId 필드가 있다고 가정
                user.getUsername(),
                3600000L  // 예시로 1시간(3600000ms) 유효기간 설정
        );

        // 사용자 정보와 반려동물 정보를 담아 반환
        return UserResponseDTO.MypageDetail.builder()
                .username(user.getUsername())
                .petDetail(convertToPetDetail(pet))
                .token(newJwtToken)
                .build();
    }


}
