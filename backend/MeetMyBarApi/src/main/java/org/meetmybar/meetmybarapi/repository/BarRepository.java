package org.meetmybar.meetmybarapi.repository;

import jakarta.inject.Inject;
import org.meetmybar.meetmybarapi.models.modif.Bar;
import org.meetmybar.meetmybarapi.models.modif.Drink;
import org.meetmybar.meetmybarapi.exception.BarNotFoundException;
import org.meetmybar.meetmybarapi.models.dto.ScheduleDay;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BarRepository {

    static final String SQL_GET_BAR =
            "SELECT id, name, capacity, address, city, postal_code FROM BAR";

    private static final String SQL_GET_BAR_BY_NAME =
            "SELECT id, name, capacity, address, city, postal_code FROM BAR WHERE name = :name";

    private static final String SQL_GET_BAR_BY_ADDRESS =
            "SELECT id, name, capacity, address, city, postal_code FROM BAR WHERE address = :address";

    private static final String SQL_GET_BAR_BY_ID =
            "SELECT id, name, capacity, address, city, postal_code FROM BAR WHERE id = :id";

    private static final String SQL_GET_BAR_DRINKS =
            "SELECT d.id, d.name, d.brand, d.degree, d.type, lbd.volume, lbd.price " +
                    "FROM DRINK d " +
                    "JOIN LINK_BAR_DRINK lbd ON d.id = lbd.id_drink " +
                    "WHERE lbd.id_bar = :barId";

    private static final String SQL_GET_BAR_SCHEDULEDAYS =
            "SELECT sd.id, sd.day, sd.opening, sd.closing " +
                    "FROM SCHEDULE_DAY sd " +
                    "JOIN LINK_BAR_SCHEDULE_DAY lbs ON sd.id = lbs.id_schedule_day " +
                    "WHERE lbs.id_bar = :barId";

    static final String SQL_INSERT_BAR =
            "INSERT INTO BAR (name, capacity, address, city, postal_code) VALUES (:name, :capacity, :address, :city, :postal_code)";

    private static final String SQL_UPDATE_BAR =
            "UPDATE BAR SET name = :name, capacity = :capacity, address = :address, " +
            "city = :city, postal_code = :postal_code WHERE id = :id";

    private static final String SQL_DELETE_BAR_PHOTOS = 
            "DELETE FROM LINK_BAR_PHOTO WHERE id_bar = :id";

    private static final String SQL_DELETE_BAR_DRINKS = 
            "DELETE FROM LINK_BAR_DRINK WHERE id_bar = :id";

    private static final String SQL_DELETE_BAR_SCHEDULES = 
            "DELETE FROM LINK_BAR_SCHEDULE_DAY WHERE id_bar = :id";

    private static final String SQL_DELETE_BAR = 
            "DELETE FROM BAR WHERE id = :id";

    @Inject
    private NamedParameterJdbcTemplate barTemplate;

    @Inject
    private ScheduleDayRepository scheduleDayRepository;

    public List<Bar> getBar() {
        try {
            HashMap<String, Object> map = new HashMap<>();
            return barTemplate.query(SQL_GET_BAR, map, (r, s) -> {
                var bar = new Bar();
                int barId = r.getInt("id");
                bar.setId(barId);
                bar.setName(r.getString("name"));
                bar.setCapacity(r.getInt("capacity"));
                bar.setAddress(r.getString("address"));
                bar.setCity(r.getString("city"));
                bar.setPostalCode(r.getString("postal_code"));

                // Récupérer les boissons du bar
                List<Drink> drinks = getDrinksForBar(barId);
                bar.setDrinks(drinks);

                // Récupérer les horaires du bar
                List<ScheduleDay> planning = getScheduleDaysForBar(barId);
                bar.setPlanning(planning);

                return bar;
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching bars: " + e.getMessage(), e);
        }
    }

    public Bar getBarByName(String barName) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", barName);

            return barTemplate.queryForObject(
                    SQL_GET_BAR_BY_NAME,
                    params,
                    (rs, rowNum) -> {
                        Bar bar = new Bar();
                        int barId = rs.getInt("id");
                        bar.setId(rs.getInt("id"));
                        bar.setName(rs.getString("name"));
                        bar.setCapacity(rs.getInt("capacity"));
                        bar.setAddress(rs.getString("address"));
                        bar.setCity(rs.getString("city"));
                        bar.setPostalCode(rs.getString("postal_code"));
                        // Récupérer les boissons du bar
                        List<Drink> drinks = getDrinksForBar(barId);
                        bar.setDrinks(drinks);

                        // Récupérer les horaires du bar
                        List<ScheduleDay> planning = getScheduleDaysForBar(barId);
                        bar.setPlanning(planning);

                        return bar;
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching bar with name " + barName + ": " + e.getMessage(), e);
        }
    }

    public Bar getBarByAddress(String barAddress) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("address", barAddress);

            return barTemplate.queryForObject(
                    SQL_GET_BAR_BY_ADDRESS,
                    params,
                    (rs, rowNum) -> {
                        Bar bar = new Bar();
                        int barId = rs.getInt("id");
                        bar.setId(rs.getInt("id"));
                        bar.setName(rs.getString("name"));
                        bar.setCapacity(rs.getInt("capacity"));
                        bar.setAddress(rs.getString("address"));
                        bar.setCity(rs.getString("city"));
                        bar.setPostalCode(rs.getString("postal_code"));
                        // Récupérer les boissons du bar
                        List<Drink> drinks = getDrinksForBar(barId);
                        bar.setDrinks(drinks);

                        // Récupérer les horaires du bar
                        List<ScheduleDay> planning = getScheduleDaysForBar(barId);
                        bar.setPlanning(planning);

                        return bar;
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching bar with address " + barAddress + ": " + e.getMessage(), e);
        }
    }

    public Bar getBarById(int barId) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", barId);

            return barTemplate.queryForObject(
                    SQL_GET_BAR_BY_ID,
                    params,
                    (rs, rowNum) -> {
                        Bar bar = new Bar();
                        bar.setId(rs.getInt("id"));
                        bar.setName(rs.getString("name"));
                        bar.setCapacity(rs.getInt("capacity"));
                        bar.setAddress(rs.getString("address"));
                        bar.setCity(rs.getString("city"));
                        bar.setPostalCode(rs.getString("postal_code"));
                        // Récupérer les boissons du bar
                        List<Drink> drinks = getDrinksForBar(barId);
                        bar.setDrinks(drinks);

                        // Récupérer les horaires du bar
                        List<ScheduleDay> planning = getScheduleDaysForBar(barId);
                        bar.setPlanning(planning);

                        return bar;
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching bar with id " + barId + ": " + e.getMessage(), e);
        }
    }

    public Bar createBar(Bar bar) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", bar.getName());
            params.put("capacity", bar.getCapacity());
            params.put("address", bar.getAddress());
            params.put("city", bar.getCity());
            params.put("postal_code", bar.getPostalCode());

            KeyHolder keyHolder = new GeneratedKeyHolder();
            barTemplate.update(SQL_INSERT_BAR, new MapSqlParameterSource(params), keyHolder, new String[]{"id"});

            Number newId = keyHolder.getKey();
            if (newId != null) {
                bar.setId(newId.intValue());
                
                // Création et liaison des ScheduleDay si présents
                if (bar.getPlanning() != null && !bar.getPlanning().isEmpty()) {
                    List<ScheduleDay> createdScheduleDays = new ArrayList<>();
                    
                    for (ScheduleDay scheduleDay : bar.getPlanning()) {
                        // Créer le ScheduleDay
                        ScheduleDay createdScheduleDay = scheduleDayRepository.createScheduleDay(scheduleDay);
                        
                        // Créer le lien entre le bar et le ScheduleDay
                        scheduleDayRepository.createBarScheduleDayLink(bar.getId(), createdScheduleDay.getId());
                        
                        createdScheduleDays.add(createdScheduleDay);
                    }
                    
                    // Mettre à jour la liste des ScheduleDay dans l'objet bar
                    bar.setPlanning(createdScheduleDays);
                }
                
                return bar;
            } else {
                throw new RuntimeException("Failed to create bar - no ID returned");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating bar: " + e.getMessage(), e);
        }
    }

    private List<Drink> getDrinksForBar(int barId) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("barId", barId);

            return barTemplate.query(SQL_GET_BAR_DRINKS, params, (r, s) -> {
                var drink = new Drink();
                drink.setId(r.getInt("id"));
                drink.setName(r.getString("name"));
                drink.setBrand(r.getString("brand"));
                drink.setAlcoholDegree(r.getDouble("degree"));
                drink.setType(Drink.TypeEnum.fromValue(r.getString("type")));
                drink.setVolume(r.getDouble("volume"));
                drink.setPrice(r.getDouble("price"));
                return drink;
            });
        } catch (Exception e) {
            // Log de l'erreur sans bloquer la récupération du bar
            System.err.println("Error fetching drinks for bar " + barId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<ScheduleDay> getScheduleDaysForBar(int barId) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("barId", barId);

            return barTemplate.query(SQL_GET_BAR_SCHEDULEDAYS, params, (r, s) -> {
                var scheduleDay = new ScheduleDay();
                scheduleDay.setId(r.getInt("id"));
                scheduleDay.setDay(r.getString("day"));
                scheduleDay.setOpening(r.getString("opening"));
                scheduleDay.setClosing(r.getString("closing"));
                return scheduleDay;
            });
        } catch (Exception e) {
            // Log de l'erreur sans bloquer la récupération du bar
            System.err.println("Error fetching schedule days for bar " + barId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Bar modifyBar(Bar bar) {
        try {    
            // Vérifier que le bar existe
            getBarById(bar.getId());

            // Préparer les paramètres
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", bar.getId());
            params.put("name", bar.getName());
            params.put("capacity", bar.getCapacity());
            params.put("address", bar.getAddress());
            params.put("city", bar.getCity());
            params.put("postal_code", bar.getPostalCode());
            // Exécuter la mise à jour
            int rowsAffected = barTemplate.update(SQL_UPDATE_BAR, params);
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No bar found with id: " + bar.getId());
            }

            // Retourner le bar mis à jour
            return getBarById(bar.getId());
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating bar: " + e.getMessage(), e);
        }
    }

    public void deleteBar(Integer barId) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", barId);
            
            // Vérifier d'abord si le bar existe
            try {
                getBarById(barId);
            } catch (Exception e) {
                throw new BarNotFoundException("Bar non trouvé avec l'id: " + barId);
            }
            
            // Supprimer les liens dans les tables de liaison
            barTemplate.update(SQL_DELETE_BAR_PHOTOS, params);
            barTemplate.update(SQL_DELETE_BAR_DRINKS, params);
            barTemplate.update(SQL_DELETE_BAR_SCHEDULES, params);
            
            // Supprimer le bar
            int rowsAffected = barTemplate.update(SQL_DELETE_BAR, params);
            
            if (rowsAffected == 0) {
                throw new BarNotFoundException("Échec de la suppression du bar avec l'id: " + barId);
            }
        } catch (BarNotFoundException e) {
            throw e; // Propager l'exception BarNotFoundException
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du bar: " + e.getMessage(), e);
        }
    }
}