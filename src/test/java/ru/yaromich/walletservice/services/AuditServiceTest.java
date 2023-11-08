package ru.yaromich.walletservice.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yaromich.walletservice.domain.dtos.AuditDto;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.domain.mappers.AuditMapper;
import ru.yaromich.walletservice.domain.models.Audit;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.logic.services.AuditServiceImpl;
import ru.yaromich.walletservice.repositories.AuditRepository;
import ru.yaromich.walletservice.repositories.UserRepository;

public class AuditServiceTest {
    private AuditRepository auditRepository;
    private UserRepository userRepository;
    private AuditMapper auditMapper;
    private AuditService auditService;
    private AuditDto auditDto;
    private Audit audit;
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        auditRepository = Mockito.mock(AuditRepository.class);
        auditMapper = Mockito.mock(AuditMapper.class);

        auditService = new AuditServiceImpl(auditRepository, userRepository, auditMapper);

        user = new User();
        user.setId(1L);
        user.setLogin("Cat with the guitar");

        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());

        auditDto = new AuditDto();
        auditDto.setUserDto(userDto);

        audit = new Audit();
        audit.setUser(user);
    }

    @Test
    public void shouldSaveAudit() {
        auditService.save(audit.getUser().getLogin(), auditDto);
    }


}
