package dopamine.backend.member.mapper;

import dopamine.backend.member.entity.Member;
import dopamine.backend.member.response.MemberResponseDto;
import dopamine.backend.member.response.MemberResponseDto.MemberResponseDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-10T17:04:10+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberResponseDto memberToMemberResponseDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponseDtoBuilder memberResponseDto = MemberResponseDto.builder();

        memberResponseDto.memberId( member.getMemberId() );
        memberResponseDto.kakaoId( member.getKakaoId() );
        memberResponseDto.nickname( member.getNickname() );
        memberResponseDto.refreshToken( member.getRefreshToken() );
        memberResponseDto.createdDate( member.getCreatedDate() );
        memberResponseDto.modifiedDate( member.getModifiedDate() );
        memberResponseDto.delYn( member.getDelYn() );

        return memberResponseDto.build();
    }
}
