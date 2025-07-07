package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PurchaseLine> lines;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private boolean paid;
    private boolean delivered;
    private boolean archived;

    public Purchase() {}

    public Purchase(User user, List<PurchaseLine> lines, Date date, boolean paid, boolean delivered, boolean archived) {
        this.user = user;
        this.lines = lines;
        this.date = date;
        this.paid = paid;
        this.delivered = delivered;
        this.archived = archived;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<PurchaseLine> getLines() { return lines; }
    public Date getDate() { return date; }
    public boolean isPaid() { return paid; }
    public boolean isDelivered() { return delivered; }
    public boolean isArchived() { return archived; }

    public void setUser(User user) { this.user = user; }
    public void setLines(List<PurchaseLine> lines) { this.lines = lines; }
    public void setDate(Date date) { this.date = date; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
    public void setArchived(boolean archived) { this.archived = archived; }

    public void setId(Long id) {
        this.id = id;
    }
}
