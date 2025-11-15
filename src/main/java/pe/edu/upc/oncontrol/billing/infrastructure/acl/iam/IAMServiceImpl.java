package pe.edu.upc.oncontrol.billing.infrastructure.acl.iam;

import pe.edu.upc.oncontrol.billing.domain.services.iam.IAMService;
import pe.edu.upc.oncontrol.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.oncontrol.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

@Service
public class IAMServiceImpl implements IAMService {

    private final IamContextFacade iamContextFace;

    public IAMServiceImpl(IamContextFacade iamContextFace) {
        this.iamContextFace = iamContextFace;
    }

    @Override
    public boolean isValidUserWithRole(Long userId, String role) {
        return iamContextFace.isValidUserWithRole(userId, Roles.valueOf(role));
    }
}
