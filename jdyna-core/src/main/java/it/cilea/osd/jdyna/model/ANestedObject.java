/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.core.TimeStampInfo;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Formula;

/**
 * 
 * @author pascarelli
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({
        @NamedQuery(name = "ANestedObject.findAll", query = "from ANestedObject order by id"),
        @NamedQuery(name = "ANestedObject.paginate.id.asc", query = "from ANestedObject order by id asc"),
        @NamedQuery(name = "ANestedObject.paginate.id.desc", query = "from ANestedObject order by id desc"),
        @NamedQuery(name = "ANestedObject.findNestedObjectsByParentIDAndTypoID", query = "from ANestedObject where parent.id = ? and typo.id = ?"),
        @NamedQuery(name = "ANestedObject.paginateNestedObjectsByParentIDAndTypoID.asc.asc", query = "from ANestedObject where parent.id = ? and typo.id = ?"),
        @NamedQuery(name = "ANestedObject.countNestedObjectsByParentIDAndTypoID", query = "select count(*) from ANestedObject where parent.id = ? and typo.id = ?"),
        @NamedQuery(name = "ANestedObject.findActiveNestedObjectsByParentIDAndTypoID", query = "from ANestedObject where parent.id = ? and typo.id = ? and status = true"),
        @NamedQuery(name = "ANestedObject.paginateActiveNestedObjectsByParentIDAndTypoID.asc.asc", query = "from ANestedObject where parent.id = ? and typo.id = ? and status = true"),
        @NamedQuery(name = "ANestedObject.countActiveNestedObjectsByParentIDAndTypoID", query = "select count(*) from ANestedObject where parent.id = ? and typo.id = ? and status = true"),
        @NamedQuery(name = "ANestedObject.findNestedObjectsByTypoID", query = "from ANestedObject where typo.id = ?"),
        @NamedQuery(name = "ANestedObject.deleteNestedObjectsByTypoID", query = "delete from ANestedObject where typo.id = ?"),
        @NamedQuery(name = "ANestedObject.maxPositionNestedObjectsByTypoID", query = "select max(position) from ANestedObject where typo.id = ?"),
        @NamedQuery(name = "ANestedObject.maxPositionNestedObjects", query = "select max(position) from ANestedObject")
})
public abstract class ANestedObject<P extends ANestedProperty<TP>, TP extends ANestedPropertiesDefinition, PP extends Property<PTP>, PTP extends PropertiesDefinition>
        extends AnagraficaObject<P, TP> implements
        Comparable<ANestedObject<P, TP, PP, PTP>>
{
    /** DB Primary key */
    @Id
    @GeneratedValue(generator = "JDYNA_NESTEDOBJECT_SEQ")
    @SequenceGenerator(name = "JDYNA_NESTEDOBJECT_SEQ", sequenceName = "JDYNA_NESTEDOBJECT_SEQ")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String uuid;

    private Boolean status = true;

    private Integer position = 0;

    /** timestamp info for creation and last modify */
    @Embedded
    private TimeStampInfo timeStampInfo;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public TimeStampInfo getTimeStampInfo()
    {
        return timeStampInfo;
    }

    public void setTimeStampInfo(TimeStampInfo timeStampInfo)
    {
        this.timeStampInfo = timeStampInfo;
    }

    public abstract AnagraficaSupport<PP, PTP> getParent();

    public abstract void setParent(
            AnagraficaSupport<? extends Property<PTP>, PTP> parent);

    public abstract Class getClassParent();

    public Boolean getStatus()
    {
        return status;
    }

    public void setStatus(Boolean status)
    {
        this.status = status;
    }

    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public int compareTo(ANestedObject<P, TP, PP, PTP> o)
    {
        if (this.getPosition() > o.getPosition())
            return 1;
        if (this.getPosition() < o.getPosition())
            return -1;
        return 0;
    }
}
