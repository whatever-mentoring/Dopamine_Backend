package dopamine.backend.challengemember.mapper;

import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.challengemember.response.ChallengeMemberResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeMemberMapper {
    ChallengeMemberResponseDto challengeMemberToChallengeMemberResponseDto(ChallengeMember challengeMember);
}