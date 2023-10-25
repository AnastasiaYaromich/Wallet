package infrastructure.in.servlets;

import aop.annotations.Speed;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.AuditDto;
import dto.TransactionDto;
import dto.TransactionInfoDto;
import infrastructure.in.utils.JwtTokenUtil;
import infrastructure.in.utils.ParseUtils;
import infrastructure.out.audit.AuditServiceSingleton;
import infrastructure.out.transaction.TransactionServiceSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AuditService;
import services.TransactionService;
import services.exceptions.TransactionException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Speed
public class TransactionServlet extends HttpServlet {

    private final AuditService auditService;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public TransactionServlet() throws SQLException {
        this.transactionService = TransactionServiceSingleton.getTransactionService();
        this.auditService = AuditServiceSingleton.getAuditService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = JwtTokenUtil.extractToken(request);
        String login = JwtTokenUtil.parseToken(token);
        String requestBody = ParseUtils.getRequestBody(request.getInputStream());
        TransactionInfoDto infoDto = null;
        String endpoint = request.getRequestURI().substring(13);
        try {
            if (endpoint.startsWith("withdraw")) {
                infoDto = objectMapper.readValue(requestBody, TransactionInfoDto.class);

                if (transactionService.isTransactionIdUnique(login, infoDto.getTransactionId())) {
                    transactionService.withdraw(login, infoDto.getAmount());
                    saveTransaction(login, infoDto.getTransactionId(), "withdraw", "success", "");
                    saveAudit(login, infoDto.getTransactionId(), "withdraw", "success", "");

                    response.setStatus(HttpServletResponse.SC_CREATED);
                    sendResponse(response, "You are successfully withdraw money. Current balance is: "
                            + transactionService.findBalanceByLogin(login) + "$");
                }
            }

            if (endpoint.startsWith("replenish")) {
                infoDto = objectMapper.readValue(requestBody, TransactionInfoDto.class);

                if (transactionService.isTransactionIdUnique(login, infoDto.getTransactionId())) {
                    transactionService.replenish(login, infoDto.getAmount());
                    saveTransaction(login, infoDto.getTransactionId(), "replenish", "success", "");
                    saveAudit(login, infoDto.getTransactionId(), "replenish", "success", "");

                    response.setStatus(HttpServletResponse.SC_CREATED);
                    sendResponse(response, "You are successfully replenish money. Current balance is: "
                            + transactionService.findBalanceByLogin(login) + "$");
                }
            }
        } catch (TransactionException e) {
                saveTransaction(login, infoDto.getTransactionId(), endpoint, "fail", e.getMessage());
                saveAudit(login, infoDto.getTransactionId(), endpoint, "fail", e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendResponse(response, e.getMessage());
        }

            if (endpoint.startsWith("history")) {
                List<TransactionDto> transactionsDto = transactionService.findAll(login);
                if (transactionsDto.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    sendResponse(response, login + " history is empty");
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    String message = this.objectMapper.writeValueAsString(transactionsDto);
                    response.getOutputStream().write(this.objectMapper.writeValueAsBytes(message));
                }
            }
    }

    private void saveTransaction(String login, String transactionId, String type, String condition, String note) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(transactionId);
        transactionDto.setType(type);
        transactionDto.setCondition(condition);
        transactionDto.setNote(note);
        transactionService.save(login, transactionDto);
    }

    private void saveAudit(String login, String transactionId, String type, String status, String note) {
        AuditDto auditDto = new AuditDto();
        auditDto.setType(type);
        auditDto.setStatus(status);
        auditDto.setDateTime(transactionDateTime());
        auditDto.setNote(note);
        auditDto.setBalance(transactionService.findBalanceByLogin(login));
        auditService.save(login, auditDto);
    }

    private String transactionDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.getStatus();
        response.setContentType("application/json");
        response.getOutputStream().write(this.objectMapper.writeValueAsBytes(message));
    }
}
