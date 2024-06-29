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

import com.uuhnaut69.ledger_core.domain.BaseEntity;
import com.uuhnaut69.ledger_core.domain.LedgerException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity {

	private Long externalId;

	private Integer code;

	private Long amount;

	public boolean canWithdraw(Long amount) {
		return this.amount >= amount;
	}

	public void deposit(Long amount) {
		this.amount += amount;
	}

	public void withdraw(Long amount) {
		if (!canWithdraw(amount)) {
			throw new LedgerException("INSUFFICIENT_BALANCE_ERROR", "Insufficient balance to withdraw");
		}
		this.amount -= amount;
	}
}
