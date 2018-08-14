package zig.i.carry.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("offer")
public class OfferAd extends Ad {
}
