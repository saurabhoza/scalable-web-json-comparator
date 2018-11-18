package com.assingment.scalableweb.service;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.assingment.scalableweb.datatransferobject.JsonResponseDTO;
import com.assingment.scalableweb.domainobject.JsonDataDO;
import com.assingment.scalableweb.enums.Side;
import com.assingment.scalableweb.exception.MissingJsonDataException;
import com.assingment.scalableweb.exception.ResourceNotFoundException;
import com.assingment.scalableweb.repository.JsonDataRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DifferenceServiceTest {

	@InjectMocks
	private DifferenceService service;

	@Mock
	public JsonDataRepository repository;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void notFound() throws Exception {
		Mockito.doReturn(Optional.empty()).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(JsonDataDO.class));
		JsonDataDO left = service.save(1L, "Left", Side.LEFT);
		assertThat(left.getId(), Matchers.is(1L));
		assertThat(left.getLeft(), Matchers.is("Left"));
		assertThat(left.getRight(), Matchers.isEmptyOrNullString());
	}
	
	@Test
	public void leftFound() throws Exception {
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, null, "Right"));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(JsonDataDO.class));
		JsonDataDO left = service.save(1L, "Left", Side.LEFT);
		assertThat(left.getId(), Matchers.is(1L));
		assertThat(left.getLeft(), Matchers.is("Left"));
		assertThat(left.getRight(), Matchers.is("Right"));
	}
	
	@Test
	public void rightFound() throws Exception {
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, "Left", null));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(JsonDataDO.class));
		JsonDataDO left = service.save(1L, "Right", Side.RIGHT);
		assertThat(left.getId(), Matchers.is(1L));
		assertThat(left.getLeft(), Matchers.is("Left"));
		assertThat(left.getRight(), Matchers.is("Right"));
	}
	
	@Test
	public void rightNotFound() throws Exception {
		Mockito.doReturn(Optional.empty()).when(repository).findById(Mockito.eq(1L));
		Mockito.doAnswer(returnsFirstArg()).when(repository).save(Mockito.any(JsonDataDO.class));
		JsonDataDO left = service.save(1L, "Right", Side.RIGHT);
		assertThat(left.getId(), Matchers.is(1L));
		assertThat(left.getRight(), Matchers.is("Right"));
		assertThat(left.getLeft(), Matchers.isEmptyOrNullString());
	}
	
	@Test
	public void diffNoDataFound() throws Exception {
		exception.expect(ResourceNotFoundException.class);
		exception.expectMessage(containsString(ResourceNotFoundException.getResourceNotFoundMessage(1L)));
		Mockito.doReturn(Optional.empty()).when(repository).findById(Mockito.eq(1L));
		service.getDifference(1L);
	}
	
	@Test
	public void diffMissingRight() throws Exception {
		exception.expect(MissingJsonDataException.class);
		exception.expectMessage(containsString(MissingJsonDataException.getMissingJsonDataMessage(1L,Side.RIGHT)));
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, "Left", null));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		service.getDifference(1L);
	}

	@Test
	public void iffMissingLeft() throws Exception {
		exception.expect(MissingJsonDataException.class);
		exception.expectMessage(containsString(MissingJsonDataException.getMissingJsonDataMessage(1L,Side.LEFT)));
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, null, "Right"));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		service.getDifference(1L);
	}
	
	@Test
	public void diffEqual() throws Exception {
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, "DBVsbG8gd29ybGJK=", "DBVsbG8gd29ybGJK="));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		JsonResponseDTO response = service.getDifference(1L);
		
		assertThat(response.getMessage(), Matchers.is(DifferenceService.EQUAL_JSON));
	}
	
	@Test
	public void diffDifferentSize() throws Exception {
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, "DBVsbG8gd29ybG=", "DBVsbG8gd29ybGJK="));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		JsonResponseDTO response = service.getDifference(1L);

		assertThat(response.getMessage(), Matchers.is(DifferenceService.EQUAL_JSON_WITH_DIFF_SIZE));
	}
	
	@Test
	public void diffDifferentOffset() throws Exception {
		Optional<JsonDataDO> document = Optional.of(new JsonDataDO(1L, "ABVsbG8gd29ybGJK=", "DBVsbG8gd29ybGJK="));
		Mockito.doReturn(document).when(repository).findById(Mockito.eq(1L));
		JsonResponseDTO response = service.getDifference(1L);
		
		assertThat(response.getMessage(), Matchers.is(String.format(DifferenceService.DIFFERENT_OFFSET, "0")));
	}
}
