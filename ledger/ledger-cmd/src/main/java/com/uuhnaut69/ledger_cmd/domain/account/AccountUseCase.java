/*
 * MIT License
 *
 * Copyright (c) 2024 Tuan Nguyen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.uuhnaut69.ledger_cmd.domain.account;

public interface AccountUseCase {

	/**
	 * Create an account
	 *
	 * @param externalId External ID
	 * @param code       Account code
	 * @param amount     Initial amount
	 * @return Account
	 */
	Account createAccount(Long externalId, Integer code, Long amount);


	/**
	 * Deposit amount to account
	 *
	 * @param accountId Account ID
	 * @param amount    Amount
	 * @return Account
	 */
	Account deposit(Long accountId, Long amount);

	/**
	 * Withdraw amount from an account
	 *
	 * @param accountId Account ID
	 * @param amount    Amount
	 * @return Account
	 */
	Account withdraw(Long accountId, Long amount);


	/**
	 * Transfer amount from an account to another account
	 *
	 * @param fromAccountId From account ID
	 * @param toAccountId   To account ID
	 * @param amount        Amount
	 * @return Account
	 */
	Account transfer(Long fromAccountId, Long toAccountId, Long amount);
}
