package br.com.api_food.application.dtos.manager;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerInputDTO {

    @Schema(description = "Manager name", example = "Carolina Herrera")
    @NotBlank
    @Size(min = 3, max = 60)
    private String name;

    @Schema(description = "Manager email", example = "carolina.herrera@getnada.com")
    @NotBlank
    @Size(min = 3, max = 60)
    private String email;

    @Schema(description = "Manager CPF number", example = "05385157830")
    @CPF
    @NotNull
    private String cpf;

    @Schema(description = "Store ID", example = "e1defa99-5e85-4b34-906e-145b1f42bd57")
    @NotNull
    private String idStore;
}