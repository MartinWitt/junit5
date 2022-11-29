/*
 * Copyright 2015-2022 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.launcher.core;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherInterceptor;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

/**
 * @since 1.10
 */
class InterceptingClosableLauncher extends DelegatingCloseableLauncher<Launcher> {

	private final LauncherInterceptor interceptor;

	InterceptingClosableLauncher(Launcher delegate, LauncherInterceptor interceptor) {
		super(delegate, it -> {
			interceptor.close();
			return it;
		});
		this.interceptor = interceptor;
	}

	@Override
	public TestPlan discover(LauncherDiscoveryRequest launcherDiscoveryRequest) {
		return interceptor.intercept(() -> super.discover(launcherDiscoveryRequest));
	}

	@Override
	public void execute(LauncherDiscoveryRequest launcherDiscoveryRequest, TestExecutionListener... listeners) {
		interceptor.intercept(() -> {
			super.execute(launcherDiscoveryRequest, listeners);
			return null;
		});
	}

	@Override
	public void execute(TestPlan testPlan, TestExecutionListener... listeners) {
		interceptor.intercept(() -> {
			super.execute(testPlan, listeners);
			return null;
		});
	}
}
