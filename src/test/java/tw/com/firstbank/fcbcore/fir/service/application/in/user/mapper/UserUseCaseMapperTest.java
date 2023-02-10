package tw.com.firstbank.fcbcore.fir.service.application.in.user.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.assertj.core.api.AssertionsForClassTypes;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import tw.com.firstbank.fcbcore.fir.service.ServiceApplication;
import tw.com.firstbank.fcbcore.fir.service.adapter.in.rest.api.CreateUserRequest;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.CreateUserRequestCommand;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.api.CreateUserResponseCommand;
import tw.com.firstbank.fcbcore.fir.service.domain.user.UserId;

@AutoConfigureMockMvc // 不會真的起servlet , 但會注入bean
@SpringBootTest(classes = ServiceApplication.class)  // 指定main
public class UserUseCaseMapperTest {

	@Autowired
	private UserUseCaseMapper mapper;

	// 使用 instancio 建立真的假實例 西班牙文 ， 實例的意思
	@Test
	public void testNoAndCreateUserRequestCommandToDto() {
		// arrange
		String no = Instancio.of(String.class).create();
		CreateUserRequestCommand createUserRequestCommand = Instancio.of(
			CreateUserRequestCommand.class).create();
		// act
		UserDto userDto = mapper.toUserDto(no, createUserRequestCommand);
		// assert
		Assertions.assertNotNull(userDto);
		Assertions.assertEquals(no, userDto.getNo());
		Assertions.assertEquals(createUserRequestCommand.getBirthday(),userDto.getBirthday());
		Assertions.assertEquals(createUserRequestCommand.getBranchCode(),userDto.getBranchCode());
		Assertions.assertEquals(createUserRequestCommand.getEmail(),userDto.getEmail());
		Assertions.assertEquals(createUserRequestCommand.getPhone(),userDto.getPhone());
		Assertions.assertEquals(createUserRequestCommand.getBusinessCategory(),userDto.getBusinessCategory());
		Assertions.assertEquals(createUserRequestCommand.getFirstName(),userDto.getFirstName());
		Assertions.assertEquals(createUserRequestCommand.getLastName(),userDto.getLastName());
		Assertions.assertEquals(createUserRequestCommand.getVerificationCode(),userDto.getVerificationCode());


		// assert 第二種方法
		assertThat(userDto)
			.usingRecursiveComparison()
			.ignoringFields("no")
			.isEqualTo(createUserRequestCommand);
	}

	@Test
	public void testStatusCodeAndNoAndBanchCodetoCreateUserResponseCommand(){
		//arrange
		String statusCode = Instancio.of(String.class).create();
		String no = Instancio.of(String.class).create();
		String branchCode = Instancio.create(String.class);

		//act
		CreateUserResponseCommand createUserResponseCommand = mapper.toCreateUserResponseCommand(
			statusCode, no, branchCode);
		//assert
		Assertions.assertNotNull(createUserResponseCommand);
		Assertions.assertEquals(statusCode,createUserResponseCommand.getStatusCode());
		Assertions.assertEquals(no, createUserResponseCommand.getNo());
		Assertions.assertEquals(branchCode,createUserResponseCommand.getBranchCode());
	}

	@Test
	public void testUserDtotoUserId(){
		//arrange
		UserDto userDto = Instancio.of(UserDto.class).create();
		//act
		UserId userId = mapper.toUserId(userDto);
		//assert
		AssertionsForClassTypes.assertThat(userId)
			.usingRecursiveComparison()
			.comparingOnlyFields("no","branchCode")
			.isEqualTo(userDto);
	}
}
