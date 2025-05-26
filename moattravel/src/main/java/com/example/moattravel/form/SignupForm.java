package com.example.moattravel.form;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignupForm {
	@NotBlank(message = "指名を入力してください")
	private String name;
	@NotBlank(message = "フリガナを入力してください")
	private String furigana;
	@NotBlank(message = "郵便番号を入力してください")
	private String postalCode;
	@NotBlank(message = "住所を入力してください")
	private String address;
	@NotBlank(message = "電話番号を入力してください")
	private String phoneNumber;
	@NotBlank(message = "メールアドレスを入力してください")
	private String email;
	@Length(min = 0, message = "パスワードは8文字以上で入力してください")
	private String password;
	@NotBlank(message = "パスワード（確認用）を入力してください")
	private String passwordConfirmation;
	
	
	
}
