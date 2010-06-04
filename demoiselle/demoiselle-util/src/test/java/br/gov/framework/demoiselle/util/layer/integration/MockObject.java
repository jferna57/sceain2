/*
 * Demoiselle Framework
 * Copyright (c) 2009 Serpro and other contributors as indicated
 * by the @author tag. See the copyright.txt in the distribution for a
 * full listing of contributors.
 *
 * Demoiselle Framework is an open source Java EE library designed to accelerate
 * the development of transactional database Web applications.
 *
 * Demoiselle Framework is released under the terms of the LGPL license 3
 * http://www.gnu.org/licenses/lgpl.html  LGPL License 3
 *
 * This file is part of Demoiselle Framework.
 *
 * Demoiselle Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License 3 as published by
 * the Free Software Foundation.
 *
 * Demoiselle Framework is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Demoiselle Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.gov.framework.demoiselle.util.layer.integration;

import java.io.FileNotFoundException;

/**
 * Represents a mocked object.
 * 
 * @author CETEC/CTJEE
 */
public class MockObject implements IMockObject {

	private int value;
	private double hiddenValue;
	private String name;

	public MockObject() {
		this.value = 0;
		this.hiddenValue = 0.0;
	}

	public MockObject(int value) {
		this.value = value;
	}

	public MockObject(int value, double hiddenValue) {
		this.value = value;
		this.hiddenValue = hiddenValue;
	}

	public MockObject(String name) {
		this.name = name.toUpperCase();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void myMethodException() throws FileNotFoundException {
		throw new FileNotFoundException();
	}

	public double getHiddenValue() {
		return hiddenValue;
	}

	public String getName() {
		return name;
	}

}
