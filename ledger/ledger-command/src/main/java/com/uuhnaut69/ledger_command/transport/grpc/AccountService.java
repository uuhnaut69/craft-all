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

package com.uuhnaut69.ledger_command.transport.grpc;

import com.uuhnaut69.ledger_command.grpc.AccountGrpc;
import com.uuhnaut69.ledger_command.grpc.AccountResponse;
import com.uuhnaut69.ledger_command.grpc.CreateAccountRequest;
import com.uuhnaut69.ledger_command.grpc.DepositRequest;
import com.uuhnaut69.ledger_command.grpc.TransferRequest;
import com.uuhnaut69.ledger_command.grpc.WithdrawRequest;
import com.uuhnaut69.ledger_domain.account.Account;
import com.uuhnaut69.ledger_domain.account.CreateAccountCommand;
import com.uuhnaut69.ledger_domain.account.DepositCommand;
import com.uuhnaut69.ledger_domain.account.TransferCommand;
import com.uuhnaut69.ledger_domain.account.WithdrawCommand;
import com.uuhnaut69.ledger_intrastructure.disruptor.CommandDispatcher;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import java.util.function.Function;

@GrpcService
public class AccountService implements AccountGrpc {

	private final CommandDispatcher<Account> accountCommandDispatcher;

	public AccountService(CommandDispatcher<Account> accountCommandDispatcher) {
		this.accountCommandDispatcher = accountCommandDispatcher;
	}

	@Override
	public Uni<AccountResponse> createAccount(CreateAccountRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new CreateAccountCommand(
								request.getExternalId(),
								request.getCode(),
								request.getLedger(),
								request.getAmount()
						)
				)
		).map(responseMapper);
	}

	@Override
	public Uni<AccountResponse> deposit(DepositRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new DepositCommand(
								request.getAccountId(),
								request.getAmount()
						)
				)
		).map(responseMapper);
	}

	@Override
	public Uni<AccountResponse> withdraw(WithdrawRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new WithdrawCommand(
								request.getAccountId(),
								request.getAmount()
						)
				)
		).map(responseMapper);
	}

	@Override
	public Uni<AccountResponse> transfer(TransferRequest request) {
		return Uni.createFrom().future(
				this.accountCommandDispatcher.dispatch(
						new TransferCommand(
								request.getFromAccountId(),
								request.getToAccountId(),
								request.getAmount()
						)
				)
		).map(responseMapper);
	}

	private final Function<Account, AccountResponse> responseMapper = account -> AccountResponse.newBuilder()
			.setId(account.getId())
			.setExternalId(account.getExternalId())
			.setCode(account.getCode())
			.setAmount(account.getAmount())
			.build();
}
