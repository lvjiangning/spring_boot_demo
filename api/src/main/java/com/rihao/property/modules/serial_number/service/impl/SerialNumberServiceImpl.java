package com.rihao.property.modules.serial_number.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.serial_number.emuns.SerialType;
import com.rihao.property.modules.serial_number.entity.SerialNumber;
import com.rihao.property.modules.serial_number.mapper.SerialNumberMapper;
import com.rihao.property.modules.serial_number.service.ISerialNumberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-10
 */
@Service
public class SerialNumberServiceImpl extends ServiceImpl<SerialNumberMapper, SerialNumber> implements ISerialNumberService {

    @Autowired
    private IEstablishService establishService;

    @Override
    public String getSettleSerialNumber() {
        return this.getNumberByType(SerialType.SETTLE, null);
    }

    @Override
    public String getContractSerialNumber(Contract contract) {
        return this.getNumberByType(contract);
    }

    @Override
    public void addSettleNo(String code) {
        this.addSerialNumber(code, SerialType.SETTLE);
    }

    @Override
    public String getSequence(Long entId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sequence = format.format(new Date());
        return entId + "-" + sequence;
    }

    @Override
    public void addContractNo(String code) {
        this.addSerialNumber(code, SerialType.CONTRACT);
    }

    /**
     * 将编号+1
     *
     * @param code
     */
    private void addSerialNumber(String code, SerialType typeId) {
        LocalDate now = LocalDate.now();
        int no = Integer.parseInt(code.substring(code.length() - 4)) + 1;
        SerialNumber serialNumber = this.getByTypeId(typeId, now);
        serialNumber.setCurrentNumber(Integer.toString(no));
        this.updateById(serialNumber);
    }

    /**
     * 得到编号中间字符，目前以 yyyy-MM为规则返回
     * 如需每个月的编号从1开始，则可以使用中间字符作为唯一约束，判断下个月重新生成记录
     *
     * @return
     */
    private String getMiddleString(LocalDate date) {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(date.atStartOfDay());
    }

    private String getNumberByType(SerialType type, String leaseStartDate) {
        LocalDate now = LocalDate.now();
        SerialNumber serialNumber = this.getByTypeId(type, LocalDate.parse(leaseStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String prefix = type.getPrefix();
        String middle = this.getMiddleString(LocalDate.parse(leaseStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        long number = Long.parseLong(serialNumber.getCurrentNumber());
        String newNumber = String.valueOf(number);
        serialNumber.setCurrentNumber((number + 1 + ""));
        this.updateById(serialNumber);
        return prefix + middle + StringUtils.leftPad(newNumber, 4, '0');
    }

    private String getNumberByType(Contract contract) {
        SerialNumber serialNumber = this.getByTypeId(SerialType.CONTRACT, LocalDate.parse(contract.getLeaseStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String prefix = establishService.getById(contract.getEstablishId()).getContractPrefix() + "-";
        String middle = this.getMiddleString(LocalDate.parse(contract.getLeaseStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        long number = Long.parseLong(serialNumber.getCurrentNumber());
        String newNumber = String.valueOf(number);
        serialNumber.setCurrentNumber((number + 1 + ""));
        this.updateById(serialNumber);
        return prefix + middle + StringUtils.leftPad(newNumber, 4, '0');
    }

    private SerialNumber getByTypeId(SerialType type, LocalDate now) {
        QueryWrapper<SerialNumber> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(SerialNumber::getTypeId, type)
                .eq(SerialNumber::getApplicableDate, now);
        SerialNumber serialNumber = this.getOne(queryWrapper);
        if (serialNumber != null) {
            return serialNumber;
        }
        serialNumber = new SerialNumber().setCurrentNumber("1")
                .setTypeId(type)
                .setApplicableDate(now);
        this.save(serialNumber);

        return serialNumber;
    }

}
