package tw.com.firstbank.fcbcore.fir.service.adapter.in.rest.api;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionsForClassTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tw.com.firstbank.fcbcore.fir.service.ServiceApplication;
import tw.com.firstbank.fcbcore.fir.service.adapter.in.rest.mapper.UserControllerMapper;
import tw.com.firstbank.fcbcore.fir.service.application.in.core.UseCaseApi;
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

@AutoConfigureMockMvc
@SpringBootTest(classes = ServiceApplication.class)
public class UserControllerApiMockBeanTest {

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

	// bean
	@Autowired
	private UserControllerMapper mapper;

	@Autowired
	private UserControllerApi userApi;

	@Test
	public void 當取得所有使用者時應要呼叫UseCase的execute() {
		//arrange
		GetUsersResponseCommand responseCommand = Instancio.create(GetUsersResponseCommand.class);
		GetUsersResponse res = mapper.toGetUsersResponse(responseCommand);
		when(getUsersUseCaseApi.execute(any())).thenReturn(responseCommand);
		//act
		GetUsersResponse realRes = userApi.getUsers();
		//assert
		assertResponse(getUsersUseCaseApi,res, realRes);
	}

	private <T> void assertResponse(UseCaseApi api , T res, T realRes) {
		verify(api, times(1)).execute(any());
		AssertionsForClassTypes.assertThat(realRes).usingRecursiveComparison()
			.isEqualTo(res);
	}


	@Test
	public void 當新增使用者時應要呼叫UseCase的execute() {
		//AAA
		//Arrange
		CreateUserResponseCommand cm = Instancio.create(CreateUserResponseCommand.class);
		CreateUserResponse res = mapper.toCreateUserResponse(cm);
		when(createUserUseCaseApi.execute(any())).thenReturn(cm);
		//Act
		CreateUserResponse realRes = userApi.createUser(any());
		//Assert
		assertResponse(createUserUseCaseApi,res, realRes);
	}

	@Test
	public void 當取得使用者時應要呼叫UseCase的execute() {
		//AAA
		//Arrange
		GetUserResponseCommand getUserResponseCommand = Instancio.create(
			GetUserResponseCommand.class);
		GetUserResponse res = mapper.toGetUserResponse(getUserResponseCommand);
		when(getUserUseCaseApi.execute(any())).thenReturn(getUserResponseCommand);
		//Act
		GetUserResponse realRes = userApi.getUser(getUserResponseCommand.getBranchCode(),
			getUserResponseCommand.getNo());
		//Assert
		assertResponse(getUserUseCaseApi,res, realRes);
	}

	@Test
	public void 當刪除使用者時應要呼叫UseCase的execute() {
		//AAA
		//Arrange
		String branchCode = Instancio.create(String.class);
		String no = Instancio.create(String.class);
		DeleteUserResponseCommand dc = Instancio.create(DeleteUserResponseCommand.class);
		DeleteUserResponse res = mapper.toDeleteUserResponse(dc);
		when(deleteUserUseCaseApi.execute(any())).thenReturn(dc);
		//Act
		DeleteUserResponse realRes = userApi.deleteUser(branchCode, no);
		//Assert
		assertResponse(deleteUserUseCaseApi,res, realRes);
	}

	@Test
	public void 當修改使用者時應要呼叫UseCase的execute() {
		//AAA
		//Arrange
		String branchCode = Instancio.create(String.class);
		String no = Instancio.of(String.class).create();
		UpdateUserResponseCommand responseCommand = Instancio.create(
			UpdateUserResponseCommand.class);
		UpdateUserResponse res = mapper.toUpdateUserResponse(responseCommand);
		when(updateUserUseCaseApi.execute(any())).thenReturn(responseCommand);
		//Act
		UpdateUserResponse realRes = userApi.updateUser(branchCode, no, any());
		//Assert
		assertResponse(updateUserUseCaseApi,res, realRes);
	}

	@Test
	public void 當使用branchCode時取得使用者時應要呼叫UseCase的execute() {
		//AAA
		//Arrange
		String branchCode = Instancio.create(String.class);
		GetUsersResponseCommand responseCommand = Instancio.create(GetUsersResponseCommand.class);
		GetUsersResponse res = mapper.toGetUsersResponse(responseCommand);
		when(getUsersByBranchCodeUseCaseApi.execute(any())).thenReturn(responseCommand);
		//Act
		GetUsersResponse realRes = userApi.getUsersByBranchCode(branchCode);
		//Assert
		assertResponse(getUsersByBranchCodeUseCaseApi,res, realRes);
	}

}
