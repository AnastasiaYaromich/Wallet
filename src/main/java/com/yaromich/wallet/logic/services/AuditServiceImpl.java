package com.yaromich.wallet.logic.services;

import com.yaromich.wallet.domain.dto.AuditDto;
import com.yaromich.wallet.domain.model.Audit;
import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.infrastructure.out.AuditRepositoryImpl;
import com.yaromich.wallet.infrastructure.out.UserRepositoryImpl;
import com.yaromich.wallet.logic.exceptions.UserException;
import com.yaromich.wallet.logic.exceptions.UserNotFoundException;
import com.yaromich.wallet.mappers.AuditMapper;
import com.yaromich.wallet.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepositoryImpl auditRepository;
    private final UserRepositoryImpl userRepository;

    @Autowired
    public AuditServiceImpl(AuditRepositoryImpl auditRepository, UserRepositoryImpl userRepository) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
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
