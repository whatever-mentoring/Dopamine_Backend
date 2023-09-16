package dopamine.backend.domain.member.mapper;

import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.domain.member.response.MemberResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberResponseDto memberToMemberResponseDto(Member member);
}