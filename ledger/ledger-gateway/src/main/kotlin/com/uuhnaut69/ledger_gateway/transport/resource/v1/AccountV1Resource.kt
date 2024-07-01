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

package com.uuhnaut69.ledger_gateway.transport.resource.v1

import com.uuhnaut69.ledger_command.grpc.*
import io.quarkus.grpc.GrpcClient
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import com.uuhnaut69.ledger_gateway.transport.resource.v1.dto.AccountResponse as RestAccountResponse
import com.uuhnaut69.ledger_gateway.transport.resource.v1.dto.CreateAccountRequest as RestCreateAccountRequest
import com.uuhnaut69.ledger_gateway.transport.resource.v1.dto.DepositRequest as RestDepositRequest
import com.uuhnaut69.ledger_gateway.transport.resource.v1.dto.TransferRequest as RestTransferRequest
import com.uuhnaut69.ledger_gateway.transport.resource.v1.dto.WithdrawRequest as RestWithdrawRequest

@Path("/api/v1/accounts")
class AccountV1Resource {

	@GrpcClient("account")
	lateinit var accountGrpcClient: AccountGrpc

	@POST
	@Path("/create")
	fun createAccount(createAccountRequest: RestCreateAccountRequest): Uni<RestAccountResponse> {
		return this.accountGrpcClient.createAccount(
			CreateAccountRequest.newBuilder()
				.setExternalId(createAccountRequest.externalId)
				.setCode(createAccountRequest.code)
				.setLedger(createAccountRequest.ledger)
				.setAmount(createAccountRequest.amount)
				.build()
		).map { it.asAccountResponse() }
	}

	@POST
	@Path("/deposit")
	fun deposit(depositRequest: RestDepositRequest): Uni<RestAccountResponse> {
		return this.accountGrpcClient.deposit(
			DepositRequest.newBuilder()
				.setAccountId(depositRequest.accountId)
				.setAmount(depositRequest.amount)
				.build()
		).map { it.asAccountResponse() }
	}

	@POST
	@Path("/withdraw")
	fun withdraw(withdrawRequest: RestWithdrawRequest): Uni<RestAccountResponse> {
		return this.accountGrpcClient.withdraw(
			WithdrawRequest.newBuilder()
				.setAccountId(withdrawRequest.accountId)
				.setAmount(withdrawRequest.amount)
				.build()
		).map { it.asAccountResponse() }
	}

	@POST
	@Path("/transfer")
	fun transfer(transferRequest: RestTransferRequest): Uni<RestAccountResponse> {
		return this.accountGrpcClient.transfer(
			TransferRequest.newBuilder()
				.setFromAccountId(transferRequest.fromAccountId)
				.setToAccountId(transferRequest.toAccountId)
				.setAmount(transferRequest.amount)
				.build()
		).map { it.asAccountResponse() }
	}

	private fun AccountResponse.asAccountResponse(): RestAccountResponse {
		return RestAccountResponse(
			id = this.id,
			externalId = this.externalId,
			code = this.code,
			ledger = this.ledger,
			amount = this.amount
		)
	}
}
