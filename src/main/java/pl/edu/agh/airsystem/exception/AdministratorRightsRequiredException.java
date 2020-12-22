package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class AdministratorRightsRequiredException extends BaseErrorException {
    public AdministratorRightsRequiredException() {
        super(FORBIDDEN,
                "ADMINISTRATOR_RIGHTS_REQUIRED",
                "To do this action you need to be administrator.");
    }
}
