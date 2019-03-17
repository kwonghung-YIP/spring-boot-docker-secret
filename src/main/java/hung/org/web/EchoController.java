package hung.org.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping
	public Date echoTime() {
		Date date = jdbcTemplate.query("SELECT SYSDATE()",new ResultSetExtractor<Date>() {

			@Override
			public Date extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				return rs.getDate(1);
			}
			
		});
		return date;
	}

}
