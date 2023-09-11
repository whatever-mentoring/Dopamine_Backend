package dopamine.backend.challengemember.mapper;

import dopamine.backend.challengemember.entity.ChallengeMember;
import dopamine.backend.challengemember.response.ChallengeMemberResponseDto;
import dopamine.backend.challengemember.response.ChallengeMemberResponseDto.ChallengeMemberResponseDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    date = "2023-09-10T17:04:10+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
=======
    date = "2023-09-10T16:36:43+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Azul Systems, Inc.)"
>>>>>>> Stashed changes
=======
    date = "2023-09-10T16:36:43+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Azul Systems, Inc.)"
>>>>>>> Stashed changes
=======
    date = "2023-09-10T16:36:43+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Azul Systems, Inc.)"
>>>>>>> Stashed changes
)
@Component
public class ChallengeMemberMapperImpl implements ChallengeMemberMapper {

    @Override
    public ChallengeMemberResponseDto challengeMemberToChallengeMemberResponseDto(ChallengeMember challengeMember) {
        if ( challengeMember == null ) {
            return null;
        }

        ChallengeMemberResponseDtoBuilder challengeMemberResponseDto = ChallengeMemberResponseDto.builder();

        challengeMemberResponseDto.challengeMemberId( challengeMember.getChallengeMemberId() );
        challengeMemberResponseDto.createdDate( challengeMember.getCreatedDate() );
        challengeMemberResponseDto.modifiedDate( challengeMember.getModifiedDate() );
        if ( challengeMember.getDelYn() != null ) {
            challengeMemberResponseDto.delYn( String.valueOf( challengeMember.getDelYn() ) );
        }

        return challengeMemberResponseDto.build();
    }
}
