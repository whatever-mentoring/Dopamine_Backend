package dopamine.backend.domain.challengemember.mapper;

import dopamine.backend.domain.challengemember.entity.ChallengeMember;
import dopamine.backend.domain.challengemember.response.ChallengeMemberResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeMemberMapper {
    ChallengeMemberResponseDto challengeMemberToChallengeMemberResponseDto(ChallengeMember challengeMember);
}