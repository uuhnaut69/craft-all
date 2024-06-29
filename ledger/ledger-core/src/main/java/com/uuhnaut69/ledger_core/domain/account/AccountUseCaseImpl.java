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

package com.uuhnaut69.ledger_core.domain.account;

import com.uuhnaut69.ledger_core.domain.LedgerException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountUseCaseImpl implements AccountUseCase {

	private final AccountRepository accountRepository;

	@Override
	public Account createAccount(Long externalId, Integer code, Long amount) {
		var account = new Account(externalId, code, amount);
		account.setId(this.accountRepository.getNextId());
		account.setTimestamp(Instant.now().getEpochSecond());
		return this.accountRepository.save(account);
	}

	@Override
	public Account deposit(Long accountId, Long amount) {
		var account = this.accountRepository.findById(accountId)
				.orElseThrow(() -> new LedgerException("ACCOUNT_NOT_FOUND", "Account not found"));
		account.deposit(amount);
		return this.accountRepository.save(account);
	}

	@Override
	public Account withdraw(Long accountId, Long amount) {
		var account = this.accountRepository.findById(accountId)
				.orElseThrow(() -> new LedgerException("ACCOUNT_NOT_FOUND", "Account not found"));
		account.withdraw(amount);
		return this.accountRepository.save(account);
	}

	@Override
	public Account transfer(Long fromAccountId, Long toAccountId, Long amount) {
		var fromAccount = this.accountRepository.findById(fromAccountId)
				.orElseThrow(() -> new LedgerException("ACCOUNT_NOT_FOUND", "Account not found"));
		var toAccount = this.accountRepository.findById(toAccountId)
				.orElseThrow(() -> new LedgerException("ACCOUNT_NOT_FOUND", "Account not found"));

		fromAccount.withdraw(amount);
		toAccount.deposit(amount);

		this.accountRepository.save(fromAccount);
		this.accountRepository.save(toAccount);
		return fromAccount;
	}
}
