package zig.i.carry.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("order")
public class OrderAd extends Ad {
}
