package org.it4innov;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Proposal {
    public Long id;
    public String firstName;
    public String messageText;
    public String dateTime;
}
