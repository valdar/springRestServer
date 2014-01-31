package it.andrea.tarocchi.restServer;

import it.andrea.tarocchi.restServer.service.IdService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.sql.DataSource;
import javax.transaction.Transaction;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.TransactionFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
	@ContextConfiguration("classpath:/META-INF/webapp/WEB-INF/datasource-test-context.xml"),
	@ContextConfiguration("classpath:/META-INF/webapp/WEB-INF/application-context.xml"),
	@ContextConfiguration("classpath:/META-INF/webapp/WEB-INF/web-context.xml")
})
public class RestServerServiceTest extends AbstractRestServerTest {
	@Autowired
    private ConfigurableApplicationContext context;
	@Autowired
	private IdService idService;
	@Autowired
	private DataSource ds;
	@Autowired
	private JmsTemplate jmsT;
	@Autowired
	private TransactionFactory tf;
	
	@Test
	public void testIdService() throws Exception {
		idService.add("1");
		Thread.sleep(1000);
		JdbcTemplate jt = new JdbcTemplate(ds);
		Assert.assertEquals(1, jt.queryForList("SELECT * FROM messages_recived").size());
		Transaction t = tf.createTransaction("test", 1000);
		Assert.assertEquals("1", retrieveMessage("queue:toBeProcessed.messages"));
		t.commit();
	}
	
	@Transactional
	private String retrieveMessage(String queueName) throws JMSException{
		TextMessage msg = (TextMessage)jmsT.receive(queueName);
		return msg.getText();
	}
	
	@Test(expected=NumberFormatException.class)
	public void testIdServiceBadValue() throws Exception {
		idService.add("1sdsd");
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void testIdServiceDuplicatedKeyValue() throws Exception {
		idService.add("1");
		Thread.sleep(1000);
		idService.add("1");
		Thread.sleep(1000);
	}
	
	@Before
	public void setUp(){
        new JdbcTemplate(ds).execute("CREATE TABLE messages_recived (id BIGINT NOT NULL, date_time TIMESTAMP, PRIMARY KEY(id))");
	}
	
	@After
	public void tearDown() throws Exception{
		new JdbcTemplate(ds).execute("DROP TABLE messages_recived");
		if(context != null && context.isActive()){  
			context.close();
		}
		deleteDirectory("activemq-data");
		deleteDirectory("id_file");
		deleteAllMatchingfilesIndir(".", "socks*.trc");
		deleteAllMatchingfilesIndir(".", "*.log");
		deleteAllMatchingfilesIndir(".", "*.epoch");
	}
}
