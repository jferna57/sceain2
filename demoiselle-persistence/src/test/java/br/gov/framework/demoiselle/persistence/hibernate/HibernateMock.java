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

import java.io.Serializable;
import java.util.ArrayList;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.transaction.ITransactionContext;
import br.gov.framework.demoiselle.util.page.Page;

/**
 * @author CETEC/CTJEE
 */
public class HibernateMock {

	ITransactionContext contextMock;
	Query queryMock;
	Page pageMock;
	SerializableStub serializableMock = new SerializableStub();
	Criteria criteriaMock;
	ScrollableResults resultsMock;
	Configuration configurationMock;
	PersistentClass persistentClassMock;
	Property propertyMock;
	Session sessionMock;
	SessionFactory sessionFactoryMock;
	Aluno aluno1,aluno2;
	Professor professor1;	
	
	public HibernateMock(){
		aluno1 = new Aluno();
		aluno1.setNome("teacher");
		aluno1.setSobrenome("sobrenome");
		professor1 = new Professor();
		professor1.setNome("teacher");
		aluno2 = new Aluno();
		aluno2.setNome("teacher2");
		aluno2.setSobrenome("sobrenome2");
	}
	
	public void reset() {

		EasyMock.reset(contextMock);

	}

	public void createConfiguration(String property, Object ipojo) {
		this.configurationMock = org.easymock.classextension.EasyMock
				.createMock(Configuration.class);

		org.easymock.classextension.EasyMock.expect(
				this.configurationMock.configure()).andReturn(
				this.configurationMock).anyTimes();
		
		org.easymock.classextension.EasyMock.expect(
				this.configurationMock.buildSessionFactory()).andReturn(
				this.sessionFactoryMock).anyTimes();

		this.persistentClassMock = org.easymock.classextension.EasyMock
				.createMock(PersistentClass.class);

		this.propertyMock = org.easymock.classextension.EasyMock
				.createMock(org.hibernate.mapping.Property.class);

		org.easymock.classextension.EasyMock.expect(propertyMock.getName())
				.andReturn("Nome");

		org.easymock.classextension.EasyMock.replay(propertyMock);

		org.easymock.classextension.EasyMock.expect(
				persistentClassMock.getIdentifierProperty()).andReturn(
				propertyMock);

		org.easymock.classextension.EasyMock.replay(persistentClassMock);

		org.easymock.classextension.EasyMock.expect(
				this.configurationMock.getClassMapping(EasyMock
						.isA(String.class))).andReturn(persistentClassMock);

		org.easymock.classextension.EasyMock.replay(this.configurationMock);

	}

	public Criteria createCriteria(ArrayList<IPojo> array) {

		Criteria criteria1 = EasyMock.createMock(Criteria.class);
		EasyMock.replay(criteria1);

		Criteria criteria = EasyMock.createNiceMock(Criteria.class);

		EasyMock.expect(criteria.add(EasyMock.isA(Example.class))).andReturn(
				criteria1).anyTimes();

		EasyMock.expect(criteria.list()).andReturn(array).anyTimes();

		this.resultsMock = EasyMock.createNiceMock(ScrollableResults.class);

		EasyMock.expect(this.resultsMock.last()).andReturn(true).anyTimes();

		EasyMock.expect(this.resultsMock.getRowNumber()).andReturn(1)
				.anyTimes();

		this.resultsMock.close();

		EasyMock.replay(this.resultsMock);

		EasyMock.expect(criteria.scroll()).andReturn(this.resultsMock)
				.anyTimes();

		EasyMock.expect(criteria.setFirstResult(EasyMock.anyInt())).andReturn(
				criteria1);

		EasyMock.expect(criteria.setMaxResults(EasyMock.anyInt())).andReturn(
				criteria1);

		EasyMock.replay(criteria);

		return criteria;
	}

	public Session createSession(ITransactionContext context,
			ArrayList<IPojo> array) {

		Session session = EasyMock.createNiceMock(Session.class);
		this.sessionMock = session;

		EasyMock.expect(session.contains(aluno1)).andReturn(true).anyTimes();
		EasyMock.expect(session.contains(aluno2)).andReturn(false).anyTimes();
		EasyMock.expect(session.contains(professor1)).andReturn(true).anyTimes();
		
		this.criteriaMock = createCriteria(array);

		queryMock = EasyMock.createNiceMock(Query.class);

		Query query1 = EasyMock.createMock(Query.class);

		EasyMock.expect(query1.scroll()).andReturn(resultsMock).anyTimes();

		EasyMock.replay(query1);

		EasyMock.expect(queryMock.scroll()).andReturn(resultsMock).anyTimes();

		EasyMock.expect(queryMock.list()).andReturn(array).anyTimes();

		EasyMock.expect(queryMock.setFirstResult(EasyMock.anyInt())).andReturn(
				query1);

		EasyMock.expect(queryMock.setMaxResults(EasyMock.anyInt())).andReturn(
				query1);

		EasyMock.replay(queryMock);

		pageMock = org.easymock.classextension.EasyMock.createMock(Page.class);

		org.easymock.classextension.EasyMock.expect(pageMock.getFirstResult())
				.andReturn(1).anyTimes();

		org.easymock.classextension.EasyMock.expect(pageMock.getMaxResults())
				.andReturn(10).anyTimes();

		org.easymock.classextension.EasyMock.replay(pageMock);

		EasyMock.expect(session.isOpen()).andReturn(true).anyTimes();
		EasyMock.expect(session.isConnected()).andReturn(true).anyTimes();
		EasyMock.expect(session.createCriteria(EasyMock.isA(Class.class)))
				.andReturn(criteriaMock).anyTimes();

		IPojo ipojo = EasyMock.createMock(IPojo.class);

		EasyMock.replay(ipojo);
		
		EasyMock.expect(
				session.get(EasyMock.not(EasyMock.eq(Class.class)), EasyMock
						.isA(Serializable.class))).andReturn(ipojo).anyTimes();
		EasyMock.expect(
				session.load(EasyMock.not(EasyMock.eq(Class.class)), EasyMock
						.isA(Serializable.class))).andReturn(ipojo).anyTimes();

		EasyMock.expect(session.createQuery(EasyMock.not(EasyMock.eq(""))))
				.andReturn(queryMock).anyTimes();

		SQLQuery sqlQuery = EasyMock.createMock(SQLQuery.class);

		EasyMock.expect(sqlQuery.list()).andReturn(array).anyTimes();

		EasyMock.expect(sqlQuery.scroll()).andReturn(resultsMock).anyTimes();

		EasyMock.expect(sqlQuery.setFirstResult(EasyMock.anyInt())).andReturn(
				queryMock);

		EasyMock.expect(sqlQuery.setMaxResults(EasyMock.anyInt())).andReturn(
				queryMock);

		EasyMock.replay(sqlQuery);

		EasyMock.expect(session.createSQLQuery(EasyMock.not(EasyMock.eq(""))))
				.andReturn(sqlQuery).anyTimes();

		session.load(ipojo, serializableMock);

		EasyMock.replay(session);

		return session;

	}
	
	public void configure(ArrayList<IPojo> array) {
		this.contextMock = createContext(this.contextMock, array);
	}

	public void configureSessionNull(ArrayList<IPojo> array) {
		this.contextMock = createContextSessionNull(this.contextMock, array);
	}

	public ITransactionContext createContext(ITransactionContext context,
			ArrayList<IPojo> array) {

		if (array == null) {
			array = new ArrayList<IPojo>();

			IPojo ipojo = EasyMock.createMock(IPojo.class);
			EasyMock.replay(ipojo);

			IPojo ipojo1 = EasyMock.createMock(IPojo.class);
			EasyMock.replay(ipojo1);

			array.add(ipojo1);

			IPojo ipojo2 = EasyMock.createMock(IPojo.class);
			EasyMock.replay(ipojo2);

		}

		this.sessionFactoryMock = EasyMock.createMock(SessionFactory.class);
		EasyMock.replay(this.sessionFactoryMock);

		if (context != null) {
			EasyMock.reset(context);
		}
		context = EasyMock.createNiceMock(ITransactionContext.class);

		HibernateTransactionResource resource = org.easymock.classextension.EasyMock
				.createMock(HibernateTransactionResource.class);

		Session session = this.createSession(context, array);

		org.easymock.classextension.EasyMock.expect(resource.getSession())
				.andReturn(session).anyTimes();

		org.easymock.classextension.EasyMock.replay(resource);

		EasyMock.expect(context.hasResource()).andReturn(false).times(1);

		EasyMock.expect(context.hasResource()).andReturn(true).anyTimes();
		context.setResource(resource);

		EasyMock.expect(context.getResource()).andReturn(resource).anyTimes();

		EasyMock.replay(context);

		return context;

	}

	public ITransactionContext createContextSessionNull(
			ITransactionContext context, ArrayList<IPojo> array) {

		if (this.contextMock != null) {
			EasyMock.resetToNice(contextMock);
		}
		context = EasyMock.createMock(ITransactionContext.class);

		if (context != null) {
			EasyMock.resetToNice(context);
		}

		HibernateTransactionResource resource = org.easymock.classextension.EasyMock
				.createMock(HibernateTransactionResource.class);

		this.sessionMock = null;

		org.easymock.classextension.EasyMock.expect(resource.getSession())
				.andReturn(this.sessionMock).anyTimes();

		org.easymock.classextension.EasyMock.replay(resource);

		EasyMock.expect(context.hasResource()).andReturn(false).anyTimes();
		context.setResource(resource);

		EasyMock.expect(context.getResource()).andReturn(null).anyTimes();

		EasyMock.replay(context);

		return context;

	}

	public ITransactionContext getContextMock() {
		return contextMock;
	}

	public void setContextMock(ITransactionContext contextMock) {
		this.contextMock = contextMock;
	}

	public Query getQueryMock() {
		return queryMock;
	}

	public void setQueryMock(Query queryMock) {
		this.queryMock = queryMock;
	}

	public Page getPageMock() {
		return pageMock;
	}

	public void setPageMock(Page pageMock) {
		this.pageMock = pageMock;
	}

	public SerializableStub getSerializableMock() {
		return serializableMock;
	}

	public void setSerializableMock(SerializableStub serializableMock) {
		this.serializableMock = serializableMock;
	}

	public Criteria getCriteriaMock() {
		return criteriaMock;
	}

	public void setCriteriaMock(Criteria criteriaMock) {
		this.criteriaMock = criteriaMock;
	}

	public ScrollableResults getResultsMock() {
		return resultsMock;
	}

	public void setResultsMock(ScrollableResults resultsMock) {
		this.resultsMock = resultsMock;
	}

	public Configuration getConfigurationMock() {
		return configurationMock;
	}

	public void setConfigurationMock(Configuration configurationMock) {
		this.configurationMock = configurationMock;
	}

	public PersistentClass getPersistentClassMock() {
		return persistentClassMock;
	}

	public void setPersistentClassMock(PersistentClass persistentClassMock) {
		this.persistentClassMock = persistentClassMock;
	}

	public Property getPropertyMock() {
		return propertyMock;
	}

	public void setPropertyMock(Property propertyMock) {
		this.propertyMock = propertyMock;
	}

	public Session getSessionMock() {
		return sessionMock;
	}

	public void setSessionMock(Session sessionMock) {
		this.sessionMock = sessionMock;
	}

	public SessionFactory getSessionFactoryMock() {
		return sessionFactoryMock;
	}

	public void setSessionFactoryMock(SessionFactory sessionFactoryMock) {
		this.sessionFactoryMock = sessionFactoryMock;
	}

	public Aluno getAluno1() {
		return aluno1;
	}

	public Aluno getAluno2() {
		return aluno2;
	}

	public Professor getProfessor1() {
		return professor1;
	}
	
}
