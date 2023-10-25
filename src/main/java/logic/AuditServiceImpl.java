package logic;

import aop.annotations.Speed;
import domain.models.Audit;
import dto.AuditDto;
import infrastructure.out.audit.AuditRepositoryImpl;
import mappers.AuditMapper;
import repositories.AuditRepository;
import services.AuditService;

import java.util.List;
import java.util.stream.Collectors;

@Speed
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public AuditDto save(String login, AuditDto auditDto) {
        Audit audit = AuditMapper.MAPPER.mapToAudit(auditDto);
        Audit savedAudit = auditRepository.save(login, audit);
        return AuditMapper.MAPPER.mapToAuditDto(savedAudit);
    }

    @Override
    public List<AuditDto> findAll(String login) {
        List<Audit> audits = auditRepository.findAll(login);
        return audits.stream().map((AuditMapper.MAPPER::mapToAuditDto))
                .collect(Collectors.toList());
    }
}
