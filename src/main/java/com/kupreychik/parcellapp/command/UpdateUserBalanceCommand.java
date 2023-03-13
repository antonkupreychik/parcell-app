package com.kupreychik.parcellapp.command;

import com.kupreychik.parcellapp.enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Update user balance command")
public class UpdateUserBalanceCommand implements Command {

    @Schema(description = "User id", example = "1")
    private Long userId;

    @Schema(description = "Balance to update", example = "100.0")
    private BigDecimal amount;

    @Schema(description = "Type of operation", example = "ADD")
    private OperationType operationType;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("userId", userId)
                .append("balance", amount)
                .append("operationType", operationType)
                .toString();
    }
}
