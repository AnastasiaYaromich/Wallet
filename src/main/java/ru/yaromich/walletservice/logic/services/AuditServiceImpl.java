package ru.yaromich.walletservice.logic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.dtos.AuditDto;
import ru.yaromich.walletservice.domain.mappers.AuditMapper;
import ru.yaromich.walletservice.domain.models.Audit;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.logic.services.exceptions.UserNotFoundException;
import ru.yaromich.walletservice.repositories.AuditRepository;
import ru.yaromich.walletservice.repositories.UserRepository;
import ru.yaromich.walletservice.services.AuditService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final UserRepository userRepository;
    private final AuditMapper auditMapper;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository, UserRepository userRepository, AuditMapper auditMapper) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
       this.auditMapper = auditMapper;
    }

    @Override
    public AuditDto save(String login, AuditDto auditDto) {
        Audit audit = AuditMapper.MAPPER.auditDtoToAudit(auditDto);
        Audit savedAudit = auditRepository.save(login, audit);
        return AuditMapper.MAPPER.auditToAuditDto(savedAudit);
    }

    @Override
    public List<AuditDto> findAllByUserName(String login) throws UserException {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Audit> audits = auditRepository.findAllByUserName(user.getLogin());
        return audits.stream().map((AuditMapper.MAPPER::auditToAuditDto)).collect(Collectors.toList());
    }
}
