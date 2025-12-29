package com.jdoc.hospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 80)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false, length = 80)
  private String firstName;

  @Column(nullable = false, length = 80)
  private String lastName;

  @Column(length = 120)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;

  @Column(nullable = false)
  private boolean enabled = true;

  public AppUser() {}

  public AppUser(String username, String passwordHash, String firstName, String lastName, String email, Role role) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.role = role;
    this.enabled = true;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }

  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }

  @Transient
  public String getFullName() {
    return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
  }
}
