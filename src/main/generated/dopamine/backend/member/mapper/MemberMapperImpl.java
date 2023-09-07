package dopamine.backend.member.mapper;

import dopamine.backend.member.dto.MemberRequestDto.Patch;
import dopamine.backend.member.dto.MemberRequestDto.Post;
import dopamine.backend.member.dto.MemberResponseDto.Response;
import dopamine.backend.member.entity.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-08T00:13:35+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Azul Systems, Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member memberPostDtoToMember(Post post) {
        if ( post == null ) {
            return null;
        }

        Member member = new Member();

        member.setId( post.getId() );
        member.setKakaoId( post.getKakaoId() );
        member.setNickname( post.getNickname() );
        member.setLevel( post.getLevel() );
        member.setBadge( post.getBadge() );
        member.setChallengeCnt( post.getChallengeCnt() );
        member.setRefreshToken( post.getRefreshToken() );

        return member;
    }

    @Override
    public Member memberPatchDtoToMember(Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Member member = new Member();

        member.setId( patch.getId() );
        member.setKakaoId( patch.getKakaoId() );
        member.setNickname( patch.getNickname() );
        member.setLevel( patch.getLevel() );
        member.setBadge( patch.getBadge() );
        member.setChallengeCnt( patch.getChallengeCnt() );
        member.setRefreshToken( patch.getRefreshToken() );

        return member;
    }

    @Override
    public Response memberToMemberResponseDto(Member member) {
        if ( member == null ) {
            return null;
        }

        Response response = new Response();

        response.setId( member.getId() );
        response.setKakaoId( member.getKakaoId() );
        response.setNickname( member.getNickname() );
        response.setLevel( member.getLevel() );
        response.setBadge( member.getBadge() );
        response.setChallengeCnt( member.getChallengeCnt() );
        response.setRefreshToken( member.getRefreshToken() );

        return response;
    }
}
