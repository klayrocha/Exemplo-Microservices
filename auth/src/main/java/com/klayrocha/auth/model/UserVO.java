package com.klayrocha.auth.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
}
