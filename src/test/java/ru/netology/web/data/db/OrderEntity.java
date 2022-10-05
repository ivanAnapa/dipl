package ru.netology.web.data.db;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderEntity {
    String id;
    String created;
    String credit_id;
    String payment_id;
}
