package net.therap.dao;

import net.therap.domain.Building;
import net.therap.domain.FlatOwner;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ashraf
 * Date: 6/7/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildingDaoImpl extends HibernateDaoSupport implements BuildingDao {

    public void saveBuilding(Building building) {
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.saveOrUpdate(building);
        session.flush();
    }

    public List<Building> getBuildingList(FlatOwner flatOwner) {
        return getHibernateTemplate().find("from Building as building where building.flatOwner = ?", new Object[]{flatOwner});
    }

    public Building getBuildingById(FlatOwner flatOwner, long id) {
        List<Building> buildingList = getHibernateTemplate().find("select building from Building as building where building.buildingId = ? and building.flatOwner = ?", new Object[]{id, flatOwner});

        if (buildingList.size() == 0) {
            return null;
        } else {
            return buildingList.get(0);
        }
    }

    public Building getBuildingById(long id) {
        return getHibernateTemplate().get(Building.class, id);
    }

    public boolean deleteBuildingById(FlatOwner flatOwner, long id) {

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Building building = getBuildingById(id);
        building.getFlatList().removeAll(building.getFlatList());
        session.update(building);
        Query query = session.createQuery("delete from Building as building where building.buildingId = :buildingId and building.flatOwner = :flatOwner");
        query.setParameter("buildingId", id);
        query.setParameter("flatOwner", flatOwner);
        return query.executeUpdate() == 0 ? false : true;
    }
}
