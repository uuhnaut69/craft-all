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

package com.uuhnaut69.ledger_intrastructure.in_memory;

import com.uuhnaut69.ledger_domain.account.Account;
import com.uuhnaut69.ledger_domain.account.AccountRepository;
import java.util.Optional;
import org.agrona.collections.Long2ObjectHashMap;
import org.agrona.collections.MutableLong;

public class AccountInMemoryRepository implements AccountRepository {

	private final MutableLong idGenerator = new MutableLong(0);

	private final Long2ObjectHashMap<Account> accounts = new Long2ObjectHashMap<>();

	@Override
	public Optional<Account> findById(Long id) {
		return Optional.ofNullable(this.accounts.get(id));
	}

	@Override
	public Account save(Account entity) {
		this.accounts.put(entity.getId(), entity);
		return entity;
	}

	@Override
	public void snapshot() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void restore() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Long getNextId() {
		return this.idGenerator.incrementAndGet();
	}
}
