package prueba;

import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;
import com.trelloapp.dto.assembler.EjemploDTOMapper;
import com.trelloapp.dto.assembler.UserDTOAssembler;

public class Pruebas {
    public static void main(String[] args) {
        Ejemplo e = new Ejemplo();
        e.setA1("a");
        e.setA2("b");

        EjemploDTOMapper ass = EjemploDTOMapper.INSTANCE;
        EjemploDTO a = ass.toDTO(e);
        System.out.println(a.getA1());

        User u = new User();
        u.setName("nombre");
        UserDTOAssembler userAss = UserDTOAssembler.INSTANCE;
        UserDTO uDto = userAss.toDTO(u, false);
        System.out.println(uDto.getName());
    }
}
