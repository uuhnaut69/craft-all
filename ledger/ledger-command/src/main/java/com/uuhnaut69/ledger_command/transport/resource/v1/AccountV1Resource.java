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

package com.uuhnaut69.ledger_command.transport.resource.v1;

import com.uuhnaut69.ledger_command.transport.resource.v1.dto.AccountResponse;
import com.uuhnaut69.ledger_command.transport.resource.v1.dto.CreateAccountRequest;
import com.uuhnaut69.ledger_command.transport.resource.v1.dto.DepositRequest;
import com.uuhnaut69.ledger_command.transport.resource.v1.dto.TransferRequest;
import com.uuhnaut69.ledger_command.transport.resource.v1.dto.WithdrawRequest;
import com.uuhnaut69.ledger_domain.account.Account;
import com.uuhnaut69.ledger_domain.account.CreateAccountCommand;
import com.uuhnaut69.ledger_domain.account.DepositCommand;
import com.uuhnaut69.ledger_domain.account.TransferCommand;
import com.uuhnaut69.ledger_domain.account.WithdrawCommand;
import com.uuhnaut69.ledger_intrastructure.disruptor.CommandDispatcher;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/api/v1/accounts")
public class AccountV1Resource {

	private final CommandDispatcher<Account> accountCommandDispatcher;

	public AccountV1Resource(CommandDispatcher<Account> accountCommandDispatcher) {
		this.accountCommandDispatcher = accountCommandDispatcher;
	}

	@POST
	@Path("/create")
	public Uni<AccountResponse> createAccount(CreateAccountRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new CreateAccountCommand(
								request.externalId(),
								request.code(),
								request.amount()
						)
				)
		).map(AccountResponse::from);
	}

	@POST
	@Path("/deposit")
	public Uni<AccountResponse> deposit(DepositRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new DepositCommand(
								request.accountId(),
								request.amount()
						)
				)
		).map(AccountResponse::from);
	}

	@POST
	@Path("/withdraw")
	public Uni<AccountResponse> withdraw(WithdrawRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new WithdrawCommand(
								request.accountId(),
								request.amount()
						)
				)
		).map(AccountResponse::from);
	}

	@POST
	@Path("/transfer")
	public Uni<AccountResponse> transfer(TransferRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new TransferCommand(
								request.fromAccountId(),
								request.toAccountId(),
								request.amount()
						)
				)
		).map(AccountResponse::from);
	}
}
