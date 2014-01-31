package it.andrea.tarocchi.restServer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import it.andrea.tarocchi.restServer.controller.IdController;
import it.andrea.tarocchi.restServer.service.IdService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
	@ContextConfiguration("classpath:/META-INF/webapp/WEB-INF/datasource-test-context.xml"),
	@ContextConfiguration("classpath:/META-INF/webapp/WEB-INF/application-context.xml"),
	@ContextConfiguration("classpath:/META-INF/webapp/WEB-INF/web-context.xml")
})
public class RestServerControllerTest extends AbstractRestServerTest {

	private MockMvc mockMvc;
	private IdService idServiceMock;
	
	@Autowired
	private IdController idc;

    @Autowired
    private WebApplicationContext webApplicationContext;
	
	@Test
	public void testIdController() throws Exception {
		
		when(idServiceMock.add("1")).thenReturn("1");

		mockMvc.perform(post("/restServer").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "1"))
		.andExpect(status().isOk())
		.andExpect(content().string("status=success"));

		verify(idServiceMock, times(1)).add("1");
		verifyNoMoreInteractions(idServiceMock);
	}
	
	@Test
	public void testIdControllerBadRequest() throws Exception {
		
		when(idServiceMock.add("1")).thenReturn("1");

		mockMvc.perform(post("/restServer").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("bad", "1"))
		.andExpect(status().isBadRequest());

		verify(idServiceMock, times(0)).add("1");
		verifyNoMoreInteractions(idServiceMock);
	}
	
	@Before
	public void setUp(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		idServiceMock = Mockito.mock(IdService.class);
		idc.setIdService(idServiceMock);
	}
	
	@After
	public void tearDown(){
		deleteDirectory("activemq-data");
		deleteDirectory("id_file");
		deleteAllMatchingfilesIndir(".", "socks*.trc");
		deleteAllMatchingfilesIndir(".", "*.log");
		deleteAllMatchingfilesIndir(".", "*.epoch");
	}
	
}
