package com.musyimi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class UserApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);

		class User{
			private Integer id;
			private String first_name;
			private String last_name;
			private String phone_number;
			private String home_residence;
			private String email;

			public User() {
			}

			public User(Integer id, String first_name, String last_name, String phone_number, String home_residence, String email) {
				this.id = id;
				this.first_name = first_name;
				this.last_name = last_name;
				this.phone_number = phone_number;
				this.home_residence = home_residence;
				this.email = email;
			}

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
			}

			public String getFirst_name() {
				return first_name;
			}

			public void setFirst_name(String first_name) {
				this.first_name = first_name;
			}

			public String getLast_name() {
				return last_name;
			}

			public void setLast_name(String last_name) {
				this.last_name = last_name;
			}

			public String getPhone_number() {
				return phone_number;
			}

			public void setPhone_number(String phone_number) {
				this.phone_number = phone_number;
			}

			public String getHome_residence() {
				return home_residence;
			}

			public void setHome_residence(String home_residence) {
				this.home_residence = home_residence;
			}

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				User user = (User) o;
				return Objects.equals(id, user.id) && Objects.equals(first_name, user.first_name) && Objects.equals(last_name, user.last_name) && Objects.equals(phone_number, user.phone_number) && Objects.equals(home_residence, user.home_residence) && Objects.equals(email, user.email);
			}

			@Override
			public int hashCode() {
				return Objects.hash(id, first_name, last_name, phone_number, home_residence, email);
			}

			@Override
			public String toString() {
				return "User{" +
						"id=" + id +
						", first_name='" + first_name + '\'' +
						", last_name='" + last_name + '\'' +
						", phone_number='" + phone_number + '\'' +
						", home_residence='" + home_residence + '\'' +
						", email='" + email + '\'' +
						'}';
			}
		}

	}

}
