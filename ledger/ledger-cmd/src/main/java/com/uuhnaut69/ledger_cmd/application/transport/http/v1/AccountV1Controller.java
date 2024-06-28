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

package com.uuhnaut69.ledger_cmd.application.transport.http.v1;

import com.uuhnaut69.ledger_cmd.application.transport.http.v1.dto.AccountResponse;
import com.uuhnaut69.ledger_cmd.application.transport.http.v1.dto.CreateAccountRequest;
import com.uuhnaut69.ledger_cmd.application.transport.http.v1.dto.DepositRequest;
import com.uuhnaut69.ledger_cmd.application.transport.http.v1.dto.TransferRequest;
import com.uuhnaut69.ledger_cmd.application.transport.http.v1.dto.WithdrawRequest;
import com.uuhnaut69.ledger_cmd.domain.account.Account;
import com.uuhnaut69.ledger_cmd.domain.account.CreateAccountCommand;
import com.uuhnaut69.ledger_cmd.domain.account.DepositCommand;
import com.uuhnaut69.ledger_cmd.domain.account.TransferCommand;
import com.uuhnaut69.ledger_cmd.domain.account.WithdrawCommand;
import com.uuhnaut69.ledger_cmd.infrastructure.queue.disruptor.CommandDispatcher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/accounts")
public class AccountV1Controller {

	private final CommandDispatcher<Account> accountCommandDispatcher;

	@Operation(summary = "Create account")
	@PostMapping
	public Mono<AccountResponse> createAccount(@RequestBody @Valid CreateAccountRequest request) {
		return this.accountCommandDispatcher
				.dispatch(new CreateAccountCommand(
						request.externalId(),
						request.code(),
						request.amount()
				))
				.map(AccountResponse::from);
	}

	@Operation(summary = "Deposit to account")
	@PostMapping("/deposit")
	public Mono<AccountResponse> deposit(@RequestBody @Valid DepositRequest request) {
		return this.accountCommandDispatcher
				.dispatch(new DepositCommand(
						request.accountId(),
						request.amount()
				))
				.map(AccountResponse::from);
	}

	@Operation(summary = "Withdraw from account")
	@PostMapping("/withdraw")
	public Mono<AccountResponse> withdraw(@RequestBody @Valid WithdrawRequest request) {
		return this.accountCommandDispatcher
				.dispatch(new WithdrawCommand(
						request.accountId(),
						request.amount()
				))
				.map(AccountResponse::from);
	}

	@Operation(summary = "Transfer between accounts")
	@PostMapping("/transfer")
	public Mono<AccountResponse> transfer(@RequestBody @Valid TransferRequest request) {
		return this.accountCommandDispatcher
				.dispatch(new TransferCommand(
						request.fromAccountId(),
						request.toAccountId(),
						request.amount()
				))
				.map(AccountResponse::from);
	}
}
