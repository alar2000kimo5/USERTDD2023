package tw.com.firstbank.fcbcore.fir.service.application.in.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.h2.command.dml.MergeUsing.When;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import tw.com.firstbank.fcbcore.fir.service.ServiceApplication;
import tw.com.firstbank.fcbcore.fir.service.adapter.out.repository.UserRepository;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.mapper.UserDto;
import tw.com.firstbank.fcbcore.fir.service.application.in.user.mapper.UserUseCaseMapper;
import tw.com.firstbank.fcbcore.fir.service.domain.user.User;

@AutoConfigureMockMvc
@SpringBootTest(classes = ServiceApplication.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserUseCaseMapper mapper;

	@MockBean  // 需要有springbootTest真的注意bean的行為
	private UserRepository userRepository;

	@Test
	public void testCreateUser() {
		//arrange
		UserDto userDto = Instancio.of(UserDto.class).create();
		User userEntity = mapper.toUserEntity(userDto);
		when(userRepository.save(any())).thenReturn(userEntity);
		//act
		UserDto realUserDto = userService.createUser(userDto);
		//assert
		verify(userRepository,times(1)).save(any());
		AssertionsForClassTypes.assertThat(realUserDto)
			.usingRecursiveComparison().isEqualTo(userDto);
	}

	@Test
	public void testGetAllUser(){
		//arrange
		List<User> userList = Instancio.of(User.class).stream().limit(10).toList();
		List<UserDto> userDtoList = userList.stream().map(mapper::toUserDto).toList();
		when(userRepository.findAll()).thenReturn(userList);
		//act
		List<UserDto> allUser = userService.getAllUser();
		//assert
		verify(userRepository,times(1)).findAll();
		AssertionsForClassTypes.assertThat(allUser)
			.usingRecursiveComparison().isEqualTo(userDtoList);
	}

}
