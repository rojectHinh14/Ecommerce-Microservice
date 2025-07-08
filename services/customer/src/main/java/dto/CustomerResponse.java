package dto;

import customer.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
public record CustomerResponse(
        String id,

        String firstname,

        String lastname,

        String email,

        Address address
) {
}
