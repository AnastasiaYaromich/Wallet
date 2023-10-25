package mappers;

import domain.models.Transaction;
import dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    TransactionDto mapToTransactionDto(Transaction transaction);
    Transaction mapToTransaction(TransactionDto transactionDto);

}
