package ru.kpfu.itis.khairullovruslan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.itis.khairullovruslan.dto.UserDTO;
import ru.kpfu.itis.khairullovruslan.dto.WeatherDTO;
import ru.kpfu.itis.khairullovruslan.service.CookieService;
import ru.kpfu.itis.khairullovruslan.service.OpenWeatherMapService;
import ru.kpfu.itis.khairullovruslan.service.WeatherLogsService;
import ru.kpfu.itis.khairullovruslan.service.impl.CookieServiceImpl;

import java.io.IOException;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {

    private OpenWeatherMapService openWeatherMapService;
    private WeatherLogsService weatherLogsService;

    private CookieService cookieService;

    @Override
    public void init() {
        openWeatherMapService = (OpenWeatherMapService) this.getServletContext().getAttribute("openWeatherMapService");
        weatherLogsService = (WeatherLogsService) this.getServletContext().getAttribute("weatherLogsService");
        cookieService = CookieServiceImpl.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cityName = req.getParameter("cityName");
        System.out.println(cityName);
        UserDTO userDTO = cookieService.findUser(req);
        if (userDTO != null) {
            WeatherDTO weatherDTO = openWeatherMapService.getWeather(cityName);
            weatherLogsService.save(userDTO.getLogin(), cityName);
            resp.setContentType("text/plain");
            resp.getWriter().write("В %s   %s градусов".formatted(cityName, weatherDTO.getMain().getTemp()));
        } else {
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }


    }
}
