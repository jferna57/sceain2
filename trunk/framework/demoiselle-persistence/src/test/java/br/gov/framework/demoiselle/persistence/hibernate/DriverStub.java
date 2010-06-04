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
package br.gov.framework.demoiselle.persistence.hibernate;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author CETEC/CTJEE
 */
public class DriverStub implements Driver {

	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Connection connect(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

}
