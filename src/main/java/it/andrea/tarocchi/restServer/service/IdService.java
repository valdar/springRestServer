package it.andrea.tarocchi.restServer.service;

import it.andrea.tarocchi.restServer.controller.IdController;

import java.sql.Types;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("idService")
public class IdService {

	private static final Logger log = Logger.getLogger(IdController.class.getName());
	
	private static final String QUEUE = "queue:toBeProcessed.messages";
	
	@Resource(name="jmsTemplate")
	JmsTemplate jmsTemplate;
	@Resource(name="myDataSource")
	DataSource dataSource;

	private final static String INSERT_QUERY = "INSERT INTO messages_recived (id, date_time) VALUES (?,?)";
	private final static int[] TYPES = new int[] { Types.BIGINT, Types.TIMESTAMP };

	@Transactional
	public String add(final String id) throws Exception{
		JdbcTemplate jt =  new JdbcTemplate(dataSource);
		Long id_long;
		try{
			id_long = Long.parseLong(id);
		}catch(Exception e){
			throw new NumberFormatException(e.getMessage());
		}

		Object[] params = new Object[] { id_long, Calendar.getInstance().getTime() };

		int row = jt.update(INSERT_QUERY, params, TYPES);
		log.info(row + " row inserted whit id: "+id_long);

		jmsTemplate.send(QUEUE, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {

				return session.createTextMessage(id);
			}
		});
		log.info("message sent whit id: "+id_long);

		return id;
	}
	
}
