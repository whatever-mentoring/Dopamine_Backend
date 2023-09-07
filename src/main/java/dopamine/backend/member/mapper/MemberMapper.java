package dopamine.backend.member.mapper;


import dopamine.backend.member.dto.MemberRequestDto;
import dopamine.backend.member.dto.MemberResponseDto;
import dopamine.backend.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberRequestDto.Post post);
    Member memberPatchDtoToMember(MemberRequestDto.Patch patch);
    MemberResponseDto.Response memberToMemberResponseDto(Member member);
}