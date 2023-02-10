package tw.com.firstbank.fcbcore.fir.service.adapter.in.rest.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import org.assertj.core.api.AssertionsForClassTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tw.com.firstbank.fcbcore.fir.service.ServiceApplication;
import tw.com.firstbank.fcbcore.fir.service.adapter.in.rest.mapper.UserControllerMapper;
import tw.com.firstbank.fcbcore.fir.service.application.in.core.UseCaseApi;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.CreateUserRequestCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.CreateUserResponseCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.CreateUserUseCaseApi;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.DeleteUserResponseCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.DeleteUserUseCaseApi;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.GetUserResponseCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.GetUserUseCaseApi;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.GetUsersByBranchCodeUseCaseApi;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.GetUsersResponseCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.GetUsersUseCaseApi;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.UpdateUserResponseCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.UpdateUserUseCaseApi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@SpringBootTest(classes = ServiceApplication.class)
public class UserControllerApiMockMvcCallApiTest {

	// bean
	@Autowired
	private UserControllerMapper mapper;

	//@Autowired
	//private UserControllerApi userApi;
	@Autowired
	private MockMvc mockMvc; // 真的從url打進去，但沒有啟動servlet

	private final static String BASE_URL = "/v1/users";

	@Autowired
	private ObjectMapper objectMapper;

	// mockbean

	@MockBean
	private CreateUserUseCaseApi createUserUseCaseApi;
	@MockBean
	private GetUserUseCaseApi getUserUseCaseApi;  // done
	@MockBean
	private GetUsersUseCaseApi getUsersUseCaseApi; // done
	@MockBean
	private DeleteUserUseCaseApi deleteUserUseCaseApi;
	@MockBean
	private UpdateUserUseCaseApi updateUserUseCaseApi;
	@MockBean
	private GetUsersByBranchCodeUseCaseApi getUsersByBranchCodeUseCaseApi;


	@Test
	public void 當取得所有使用者時應要呼叫UseCase的execute() throws Exception {
		//arrange
		GetUsersResponseCommand responseCommand = Instancio.create(GetUsersResponseCommand.class);
		when(getUsersUseCaseApi.execute(any())).thenReturn(responseCommand);
		String expected = objectMapper.writeValueAsString(
			mapper.toGetUsersResponse(responseCommand));
		//act
		//GetUsersResponse realRes = userApi.getUsers();
		ResultActions resultActions = mockMvc.perform(get(BASE_URL));

		//assert
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

	@Test
	public void 當新增使用者時應要呼叫UseCase的execute() throws Exception {
		//AAA
		//Arrange
		CreateUserRequest createUserRequest = Instancio.of(CreateUserRequest.class).create();
		String requestString = objectMapper.writeValueAsString(createUserRequest);

		CreateUserResponseCommand cm = Instancio.create(CreateUserResponseCommand.class);
		when(createUserUseCaseApi.execute(any())).thenReturn(cm);
		String expected = objectMapper.writeValueAsString(mapper.toCreateUserResponse(cm));
		//Act
		ResultActions resultActions = mockMvc.perform(post(BASE_URL)
			.content(requestString).contentType(MediaType.APPLICATION_JSON));

		//Assert
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

	@Test
	public void 當取得使用者時應要呼叫UseCase的execute() throws Exception {
		//AAA
		//Arrange
		GetUserResponseCommand getUserResponseCommand = Instancio.create(
			GetUserResponseCommand.class);
		GetUserResponse res = mapper.toGetUserResponse(getUserResponseCommand);
		when(getUserUseCaseApi.execute(any())).thenReturn(getUserResponseCommand);
		String expected = objectMapper.writeValueAsString(res);

		String url = BASE_URL + "/" + getUserResponseCommand.getBranchCode() + "/"
			+ getUserResponseCommand.getNo() + "";
		//Act
		ResultActions resultActions = mockMvc.perform(
			get(url));
		//Assert
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(expected));

	}

	@Test
	public void 當刪除使用者時應要呼叫UseCase的execute() throws Exception {
		//AAA
		//Arrange
		String branchCode = Instancio.create(String.class);
		String no = Instancio.create(String.class);
		DeleteUserResponseCommand dc = Instancio.create(DeleteUserResponseCommand.class);
		String expected = objectMapper.writeValueAsString(mapper.toDeleteUserResponse(dc));
		when(deleteUserUseCaseApi.execute(any())).thenReturn(dc);

		String url = BASE_URL + "/" + branchCode + "/"
			+ no + "";
		//Act
		ResultActions resultActions = mockMvc.perform(delete(url));

		//Assert
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

	@Test
	public void 當修改使用者時應要呼叫UseCase的execute() throws Exception {
		//AAA
		String branchCode = Instancio.create(String.class);
		String no = Instancio.of(String.class).create();
		UpdateUserResponseCommand responseCommand = Instancio.create(
			UpdateUserResponseCommand.class);
		UpdateUserResponse res = mapper.toUpdateUserResponse(responseCommand);
		String expected = objectMapper.writeValueAsString(res);
		when(updateUserUseCaseApi.execute(any())).thenReturn(responseCommand);
		UpdateUserRequest request = Instancio.create(UpdateUserRequest.class);
		String requestString =  objectMapper.writeValueAsString(request);
		String url = BASE_URL + "/" + branchCode + "/"
			+ no + "";
		//Act
		ResultActions resultActions = mockMvc.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON).content(requestString));
		//Assert
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

	@Test
	public void 當使用branchCode時取得使用者時應要呼叫UseCase的execute() throws Exception {
		//AAA
		//Arrange
		String branchCode = Instancio.create(String.class);
		GetUsersResponseCommand responseCommand = Instancio.create(GetUsersResponseCommand.class);
		GetUsersResponse res = mapper.toGetUsersResponse(responseCommand);
		String expected = objectMapper.writeValueAsString(res);
		when(getUsersByBranchCodeUseCaseApi.execute(any())).thenReturn(responseCommand);
		String url = BASE_URL + "/" + branchCode + "/";
		//Act
		ResultActions resultActions = mockMvc.perform(get(url));
		//Assert
		resultActions.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

}
