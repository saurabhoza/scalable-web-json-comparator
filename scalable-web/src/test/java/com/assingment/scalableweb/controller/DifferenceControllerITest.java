package com.assingment.scalableweb.controller;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.assingment.scalableweb.datatransferobject.JsonRequestDTO;
import com.assingment.scalableweb.domainobject.JsonDataDO;
import com.assingment.scalableweb.enums.Side;
import com.assingment.scalableweb.exception.MissingJsonDataException;
import com.assingment.scalableweb.exception.ResourceNotFoundException;
import com.assingment.scalableweb.repository.JsonDataRepository;
import com.assingment.scalableweb.service.DifferenceService;
import com.assingment.scalableweb.util.TestUtil;

/**
 * This class contains the integration test cases of {@link DifferenceController}
 * 
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DifferenceControllerITest {
	
	private static final String EMPTY_JSON_MESSAGE = "Data field must not be empty.";

	private static final String JSON_BASE64 = "dGVzdGluZyB0aGUgYmFzZTY0";

	@Autowired
	private MockMvc mvc;

	@Autowired
	public JsonDataRepository repository;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private JsonRequestDTO validJsonRequest = new JsonRequestDTO(JSON_BASE64);

	@Before
	public void setup() throws Exception {
		this.mvc = webAppContextSetup(webApplicationContext).build();
		this.repository.deleteAll();
	}

	@Test
	public void insertEqualJsonStrings() throws Exception {
		insertLeftSide_withValidJsonString();
		insertRightSide_withValidJsonString();
	}
	
	@Test
	public void insertLeftSide_withEmptyJsonString() throws Exception {
		insertInvalidJsonString("/left", new JsonRequestDTO(""), EMPTY_JSON_MESSAGE);
	}

	@Test
	public void insertLeftSide_withNullJsonString() throws Exception {
		insertInvalidJsonString("/left", new JsonRequestDTO(null), EMPTY_JSON_MESSAGE);
	}
	
	@Test
	public void insertRightSide_withEmptyJsonString() throws Exception {
		insertInvalidJsonString("/right", new JsonRequestDTO(""), EMPTY_JSON_MESSAGE);
	}

	@Test
	public void insertRightSide_withNullJsonString() throws Exception {
		insertInvalidJsonString("/right", new JsonRequestDTO(null), EMPTY_JSON_MESSAGE);
	}
	
	@Test
	public void insertLeftSide_withInvalidBase64JsonString() throws Exception {
		insertInvalidJsonString("/left", new JsonRequestDTO("ffdff...&**"), "Input String is not in a valid Base 64 JSON format.");
	}
	
	@Test
	public void insertRightSide_withInvalidBase64JsonString() throws Exception {
		insertInvalidJsonString("/right", new JsonRequestDTO("ffdff...&**"), "Input String is not in a valid Base 64 JSON format.");
	}
	
	@Test
	public void checkDifference_withEqualJsonStrings() throws Exception {
		repository.save(new JsonDataDO(1l, JSON_BASE64, JSON_BASE64));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is(DifferenceService.EQUAL_JSON_SUCCESS_MESSAGE)))
			.andReturn();		
	}
	
	@Test
	public void checkDifference_withDifferentSizeJsonStrings() throws Exception {
		repository.save(new JsonDataDO(1l, JSON_BASE64, "dGhlIDJuZCB0ZXN0IGZvciBjb21wYXJpbmc="));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is(DifferenceService.EQUAL_JSON_WITH_DIFFERENT_SIZE)))
			.andReturn();		
	}
	
	@Test
	public void checkDifference_withDifferentOffsetJsonStrings() throws Exception {
		repository.save(new JsonDataDO(1l, JSON_BASE64, "dAVBdGluZyB0aGUgYmFzZTY0"));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is(String.format(DifferenceService.DIFFERENT_OFFSET, "1 3"))))
			.andReturn();		
	}
	
	@Test
	public void checkDifference_withOnlyLeftSideOfJsonData() throws Exception {
		repository.save(new JsonDataDO(2L, JSON_BASE64, ""));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/2"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title", is("Missing Data")))
			.andExpect(jsonPath("$.details", is(MissingJsonDataException.getMissingJsonDataMessage(2L, Side.RIGHT))))
			.andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
			.andReturn();			
	}
	
	@Test
	public void checkDifference_withOnlyRightSideOfJsonData() throws Exception {
		repository.save(new JsonDataDO(2L, "", JSON_BASE64));
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/2"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title", is("Missing Data")))
			.andExpect(jsonPath("$.details", is(MissingJsonDataException.getMissingJsonDataMessage(2L, Side.LEFT))))
			.andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
			.andReturn();			
	}
	
	@Test
	public void checkDifference_withInvalidId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/v1/diff/2"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title", is("Resource Not Found")))
			.andExpect(jsonPath("$.details", is(ResourceNotFoundException.getResourceNotFoundMessage(2L))))
			.andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
			.andReturn();			
	}
	
	@Test
	public void insertRightSide_withInvalidInputObject() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/v1/diff/1/right")
				.contentType(MediaType.APPLICATION_JSON)
				.content("Invalid Input"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title", is("Message Not Readable")))
				.andReturn();
	}
	
	private void insertLeftSide_withValidJsonString() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/v1/diff/1/left")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(validJsonRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message", is(DifferenceController.getSuccessResponse(Side.LEFT))))
				.andReturn();
		
		JsonDataDO jsonData = repository.findById(1L).orElse(null);
		assertThat(jsonData.getId(), Matchers.is(1L));
		assertThat(jsonData.getLeft(), Matchers.is(JSON_BASE64));
		assertThat(jsonData.getRight(), Matchers.isEmptyOrNullString());
	}
	
	private void insertRightSide_withValidJsonString() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/v1/diff/1/right").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(validJsonRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message", is(DifferenceController.getSuccessResponse(Side.RIGHT))))
				.andReturn();
		
		JsonDataDO jsonData = repository.findById(1L).orElse(null);
		assertThat(jsonData.getId(), Matchers.is(1L));
		assertThat(jsonData.getRight(), Matchers.is(JSON_BASE64));
		assertThat(jsonData.getLeft(), Matchers.is(JSON_BASE64));
	}
	
	private void insertInvalidJsonString(String side, JsonRequestDTO request, String errorMessage) throws Exception, IOException {
		mvc.perform(MockMvcRequestBuilders.post("/v1/diff/1"+side)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title", is("Constraints Validation Failed")))
				.andExpect(jsonPath("$.details", is(errorMessage)))
				.andReturn();
		
		JsonDataDO jsonData = repository.findById(1L).orElse(null);
		assertThat(jsonData, is(nullValue()));
	}
}
